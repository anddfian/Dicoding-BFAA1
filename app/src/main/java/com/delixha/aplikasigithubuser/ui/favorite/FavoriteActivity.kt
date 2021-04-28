package com.delixha.aplikasigithubuser.ui.favorite

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.delixha.aplikasigithubuser.R
import com.delixha.aplikasigithubuser.data.adapter.UserAdapter
import com.delixha.aplikasigithubuser.data.db.UserContract.UserColums.Companion.CONTENT_URI
import com.delixha.aplikasigithubuser.data.entity.User
import com.delixha.aplikasigithubuser.databinding.ActivityFavoriteBinding
import com.delixha.aplikasigithubuser.data.db.UserHelper
import com.delixha.aplikasigithubuser.data.helper.MappingHelper
import com.delixha.aplikasigithubuser.ui.favorite.detail.DetailFavoriteActivity
import com.delixha.aplikasigithubuser.ui.setting.SettingsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()

        initHandler()

        if (savedInstanceState == null) {
            loadUsersAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listUsers = list
            }
        }

        initActionBar()
    }

    private fun initHandler() {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUsersAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun initAdapter() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        adapter = UserAdapter()
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val detailFavoriteActivityIntent = Intent(this@FavoriteActivity, DetailFavoriteActivity::class.java)
                detailFavoriteActivityIntent.putExtra(DetailFavoriteActivity.EXTRA_USER, data)
                startActivity(detailFavoriteActivityIntent)
            }
        })
    }

    private fun initActionBar() {
        this.title = resources.getString(R.string.titlefavorite)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUsers)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(this@FavoriteActivity, SettingsActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val users = deferredUsers.await()
            if (users.size > 0) {
                adapter.listUsers = users
            } else {
                adapter.listUsers = ArrayList()
                Snackbar.make(binding.rvUsers, R.string.no_favorite, Snackbar.LENGTH_SHORT).show()
            }
            userHelper.close()
        }
    }
}
package com.delixha.aplikasigithubuser.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.delixha.aplikasigithubuser.ui.detail.DetailActivity
import com.delixha.aplikasigithubuser.ui.favorite.FavoriteActivity
import com.delixha.aplikasigithubuser.R
import com.delixha.aplikasigithubuser.ui.adapter.UserAdapter
import com.delixha.aplikasigithubuser.data.entity.User
import com.delixha.aplikasigithubuser.databinding.ActivityMainBinding
import com.delixha.aplikasigithubuser.ui.setting.SettingsActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initViewModel()
        observeData()
    }

    private fun initAdapter() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val detailActivityIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailActivityIntent.putExtra(DetailActivity.EXTRA_USER, data.username)
                startActivity(detailActivityIntent)
            }
        })
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
    }

    private fun observeData() {
        var firstLoad = false
        mainViewModel.setUser("")
        mainViewModel.getUser().observe(this, { user ->
            if (user != null) {
                if(firstLoad && user.size == 0) {
                    Snackbar.make(binding.rvUsers, R.string.no_user, Snackbar.LENGTH_LONG).show()
                } else {
                    firstLoad = true
                }
                adapter.setData(user)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                showLoading(true)
                mainViewModel.setUser(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_show_favorite) {
            val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(mIntent)
        } else if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
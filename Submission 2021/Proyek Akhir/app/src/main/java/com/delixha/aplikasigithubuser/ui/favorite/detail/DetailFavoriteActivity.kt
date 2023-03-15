package com.delixha.aplikasigithubuser.ui.favorite.detail

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.delixha.aplikasigithubuser.R
import com.delixha.aplikasigithubuser.data.db.UserContract
import com.delixha.aplikasigithubuser.data.entity.User
import com.delixha.aplikasigithubuser.data.helper.MappingHelper
import com.delixha.aplikasigithubuser.databinding.ActivityDetailFavoriteBinding
import com.delixha.aplikasigithubuser.ui.adapter.SectionsPagerAdapter
import com.delixha.aplikasigithubuser.ui.setting.SettingsActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailFavoriteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private var statusFavorite = false
    private lateinit var binding: ActivityDetailFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent?.getParcelableExtra<User>(EXTRA_USER) as User
        showDataDetailUser(user)
        initAdapter(user.username)
        initActionBar(user.username)

        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(UserContract.UserColums.CONTENT_URI, null, null, null, null )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            for (data in notes) {
                if (data.username == user.username) {
                    statusFavorite = true
                    setStatusFavorite(statusFavorite)
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
            if (statusFavorite) {
                val values = ContentValues()
                values.put(UserContract.UserColums.USERNAME, user.username)
                values.put(UserContract.UserColums.NAME, user.name)
                values.put(UserContract.UserColums.LOCATION, user.location)
                values.put(UserContract.UserColums.REPOSITORY, user.repository)
                values.put(UserContract.UserColums.COMPANY, user.company)
                values.put(UserContract.UserColums.AVATAR_URL, user.avatar)
                contentResolver.insert(UserContract.UserColums.CONTENT_URI, values)
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    val deferredNotes = async(Dispatchers.IO) {
                        val cursor = contentResolver.query(UserContract.UserColums.CONTENT_URI, null, null, null, null )
                        MappingHelper.mapCursorToArrayList(cursor)
                    }
                    val notes = deferredNotes.await()
                    for (data in notes) {
                        if (data.username == user.username) {
                            val uriWithId = Uri.parse(UserContract.UserColums.CONTENT_URI.toString() + "/" + data.id)
                            contentResolver.delete(uriWithId, null, null)
                        }
                    }
                }
            }
        }
    }

    private fun initActionBar(user: String?) {
        this.title = resources.getString(R.string.titlebar).format(user)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initAdapter(user: String?) {
        val sectionPagerAdapter = SectionsPagerAdapter(this)
        sectionPagerAdapter.username = user
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
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
            val mIntent = Intent(this@DetailFavoriteActivity, SettingsActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDataDetailUser(user: User) {
        with(binding) {
            Glide.with(this@DetailFavoriteActivity)
                .load(user.avatar)
                .into(avatarUser)
            tvName.text = user.name
            if(user.company != "null") {
                tvCompany.text = user.company
            } else {
                tvCompany.text = "-"
            }
            if(user.location != "null") {
                tvLocation.text = user.location
            } else {
                tvLocation.text = "-"
            }
            tvRepository.text = user.repository.toString()
        }
    }
}
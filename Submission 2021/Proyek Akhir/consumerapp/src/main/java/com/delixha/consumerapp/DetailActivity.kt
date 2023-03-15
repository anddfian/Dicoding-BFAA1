package com.delixha.consumerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.delixha.consumerapp.databinding.ActivityDetailBinding
import com.delixha.consumerapp.entity.User

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent?.getParcelableExtra<User>(EXTRA_USER) as User
        with(binding) {
            com.bumptech.glide.Glide.with(this@DetailActivity)
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

        initActionBar(user.username)
    }

    private fun initActionBar(username: String?) {
        this.title = resources.getString(R.string.titlefavoritedetail).format(username)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
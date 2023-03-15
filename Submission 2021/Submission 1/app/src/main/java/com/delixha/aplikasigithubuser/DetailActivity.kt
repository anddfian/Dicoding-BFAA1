package com.delixha.aplikasigithubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvName: TextView = findViewById(R.id.tv_name)
        val tvLocation: TextView = findViewById(R.id.tv_location)
        val tvRepository: TextView = findViewById(R.id.tv_repository)
        val tvCompany: TextView = findViewById(R.id.tv_company)
        val tvFollowers: TextView = findViewById(R.id.tv_followers)
        val tvFollowing: TextView = findViewById(R.id.tv_following)
        val tvAvatar: ImageView = findViewById(R.id.avatar_user)
        val btnShare: Button = findViewById(R.id.btn_share)
        btnShare.setOnClickListener(this)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        this.title = "Detail User - ${user.username}"
        tvName.text = user.name
        tvLocation.text = user.location
        tvRepository.text = user.repository
        tvCompany.text = user.company
        tvFollowers.text = user.followers
        tvFollowing.text = user.following
        Glide.with(this).load(user.avatar).into(tvAvatar)
    }

    override fun onClick(v: View) {
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        if (v.id == R.id.btn_share) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, "Github User ${user.name}")
                putExtra(Intent.EXTRA_TEXT, "https://github.com/${user.username}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
}
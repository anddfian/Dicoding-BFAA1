package com.delixha.aplikasigithubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var users = arrayListOf<User>()

    override fun getCount(): Int = users.size

    override fun getItem(i: Int): Any = users[i]

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    private inner class ViewHolder constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.txt_name)
        private val txtUsername: TextView = view.findViewById(R.id.txt_username)
        private val imgAvatar: ImageView = view.findViewById(R.id.img_avatar)
        fun bind(user: User) {
            txtName.text = user.name
            txtUsername.text = user.username
            Glide.with(context).load(user.avatar).into(imgAvatar)
        }
    }
}
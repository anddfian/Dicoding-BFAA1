package com.delixha.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.delixha.githubuser.R
import com.delixha.githubuser.data.entity.User
import com.delixha.githubuser.databinding.ItemUserBinding

class ListUserAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        return ListViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }

    override fun getItemCount() = listUser.size

    inner class ListViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(binding.root)
                .load(user.avatar)
                .into(binding.civItemAvatar)
            val username = itemView.context.getString(R.string.username_format, user.username)
            binding.tvItemUsername.text = username
            binding.tvItemName.text = user.username
            binding.root.setOnClickListener { onItemClickCallback.onItemClicked(user.username.toString()) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }
}

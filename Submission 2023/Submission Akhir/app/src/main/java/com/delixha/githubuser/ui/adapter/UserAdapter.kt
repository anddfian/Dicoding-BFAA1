package com.delixha.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.delixha.githubuser.R
import com.delixha.githubuser.data.local.entity.UserEntity
import com.delixha.githubuser.databinding.ItemUserBinding

class UserAdapter(private val listUserEntity: ArrayList<UserEntity>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

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
        val user = listUserEntity[position]
        holder.bind(user)
    }

    override fun getItemCount() = listUserEntity.size

    inner class ListViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userEntity: UserEntity) {
            Glide.with(binding.root)
                .load(userEntity.avatarUrl)
                .placeholder(R.drawable.ic_loading)
                .into(binding.civItemAvatar)
            val username = itemView.context.getString(R.string.username_format, userEntity.username)
            binding.tvItemUsername.text = username
            binding.tvItemName.text = userEntity.username
            binding.root.setOnClickListener { onItemClickCallback.onItemClicked(userEntity.username) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}


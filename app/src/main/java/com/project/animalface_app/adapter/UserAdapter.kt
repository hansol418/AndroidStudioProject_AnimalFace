package com.project.animalface_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.animalface_app.R
import com.project.animalface_app.databinding.ItemViewBinding
import com.project.animalface_app.dto.UserItem

class UserAdapter : PagingDataAdapter<UserItem, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class UserViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserItem) {
            binding.itemUsername.text = user.memberName
            binding.itemName.text = user.memberId

            val imageUrl = "http://10.100.201.41:8080/api/users/${user.memberNo}/profileImage"
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.user_basic)
                .into(binding.itemProfileImage)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.memberNo == newItem.memberNo
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
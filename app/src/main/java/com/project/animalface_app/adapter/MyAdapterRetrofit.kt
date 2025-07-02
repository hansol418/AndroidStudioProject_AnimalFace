package com.project.animalface_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.animalface_app.R
import com.project.animalface_app.databinding.ItemViewBinding
import com.project.animalface_app.dto.UserItem

class MyViewHolderRetrofit(val binding: ItemViewBinding)
    : RecyclerView.ViewHolder(binding.root)

class MyAdapterRetrofit(val context: Context, val datas: List<UserItem>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return MyViewHolderRetrofit(
            ItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolderRetrofit).binding
        val item = datas?.get(position)
        binding.itemUsername.text = item?.memberId
        binding.itemName.text = item?.memberName


        val imageUrl = "http://10.100.201.41:8080/api/users/${item?.memberNo}/profileImage"
        Glide.with(context)

            .load(imageUrl)
            .override(300,300)
            .placeholder(R.drawable.test)
            .error(R.drawable.user_basic)
            .into(binding.itemProfileImage)
    }

}









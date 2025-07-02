package com.project.animalface_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    private var itemList: List<String>,
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView.text = item

        holder.itemView.setOnClickListener {
            itemClickListener(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateData(newItemList: List<String>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}

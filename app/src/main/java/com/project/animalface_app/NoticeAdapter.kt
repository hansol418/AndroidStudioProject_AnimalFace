package com.project.animalface_app

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class NoticeAdapter(
    private var notices: List<Notice>,
    private val onClick: (Notice) -> Unit
) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    class NoticeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noticeNo: TextView = view.findViewById(R.id.noticeNo)
        val noticeName: TextView = view.findViewById(R.id.noticeName)
        val noticeContents: TextView = view.findViewById(R.id.noticeContents)
        val noticeDate: TextView = view.findViewById(R.id.noticeDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val notice = notices[position]
        holder.noticeNo.text = notice.noticeNo.toString()
        holder.noticeName.text = notice.noticeName
        holder.noticeContents.text = notice.noticeContents
        holder.noticeDate.text = notice.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        holder.itemView.setOnClickListener {
            onClick(notice)
        }
    }

    override fun getItemCount(): Int = notices.size

    fun updateData(newNotices: List<Notice>) {
        notices = newNotices
        notifyDataSetChanged()
    }
}

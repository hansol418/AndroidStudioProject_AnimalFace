package com.project.animalface_app

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

class NoticeFragment : Fragment() {

    private lateinit var noticeApi: NoticeApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoticeAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notice, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NoticeAdapter(emptyList()) { notice ->
            val fragment = DataFragment.newInstance(notice.noticeNo)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter

        noticeApi = RetrofitClient.instance.create(NoticeApiService::class.java)

        getNotices()

        return view
    }

    private fun getNotices() {
        noticeApi.getNotices().enqueue(object : Callback<List<Notice>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<Notice>>,
                response: Response<List<Notice>>
            ) {
                if (response.isSuccessful) {
                    val notices = response.body() ?: emptyList()
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    notices.forEach { notice ->
                        notice.date?.let {
                            val formattedDate = it.format(dateFormatter)
                        }
                    }
                    (recyclerView.adapter as NoticeAdapter).updateData(notices)
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Notice>>, t: Throwable) {
                Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
package com.project.animalface_app

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

class DataFragment : Fragment() {

    private lateinit var noticeApi: NoticeApiService
    private var viewReference: View? = null

    companion object {
        private const val ARG_NOTICE_ID = "notice_id"

        fun newInstance(noticeNo: Long): DataFragment {
            val fragment = DataFragment()
            val args = Bundle()
            args.putLong(ARG_NOTICE_ID, noticeNo)
            fragment.arguments = args
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        viewReference = view

        val noticeNo = arguments?.getLong(ARG_NOTICE_ID) ?: return view

        noticeApi = RetrofitClient.instance.create(NoticeApiService::class.java)

        getNoticeByNo(noticeNo)

        val backButton: ImageButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            (activity as? NoticeMainActivity)?.showNoticeUI()
            fragmentManager?.popBackStack()
        }

        return view
    }

    private fun getNoticeByNo(noticeNo: Long) {
        noticeApi.getNoticeByNo(noticeNo).enqueue(object : Callback<Notice> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Notice>, response: Response<Notice>) {
                if (response.isSuccessful) {
                    val notice = response.body()
                    viewReference?.findViewById<TextView>(R.id.noticeName)?.text = notice?.noticeName
                    viewReference?.findViewById<TextView>(R.id.noticeContents)?.text = notice?.noticeContents
                    notice?.date?.let {
                        viewReference?.findViewById<TextView>(R.id.noticeDate)?.text = it.format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Notice>, t: Throwable) {
                Toast.makeText(requireContext(), "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}

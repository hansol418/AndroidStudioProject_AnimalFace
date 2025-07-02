package com.project.animalface_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NoticeApiService {
    @GET("api/notices")
    fun getNotices(): Call<List<Notice>>

    @GET("api/notices/{noticeNo}")
    fun getNoticeByNo(@Path("noticeNo") noticeNo: Long): Call<Notice>

}
package com.project.animalface_app;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class NoticeData {

    @SerializedName("noticeNo")
    private Long noticeNo;
    @SerializedName("noticeName")
    private String noticeName;
    @SerializedName("noticeContents")
    private String noticeContents;
    @SerializedName("date")
    private LocalDate date;

    public NoticeData(Long noticeNo, String noticeName, String noticeContents, LocalDate date){
        this.noticeNo = noticeNo;
        this.noticeName = noticeName;
        this.noticeContents = noticeContents;
        this.date = date;
    }
}

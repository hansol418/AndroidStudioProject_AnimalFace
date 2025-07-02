package com.project.animalface_app

import java.time.LocalDate

data class Notice(    val noticeNo: Long,
                      val noticeName: String,
                      val noticeContents: String,
                      val date: LocalDate
)

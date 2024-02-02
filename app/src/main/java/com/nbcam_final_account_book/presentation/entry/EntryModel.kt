package com.nbcam_final_account_book.presentation.entry

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntryModel(
    val id: Long = 0, // 고유 식별자
    val type: String, // 수입 지출 구분 태그
    val dateTime: String,
    val value: String, // 수입/지출 값(ex. 10,000원)
    val tag: String, // ex. 교통비
    val title: String, // 수입/지출 제목 (ex. 택시)
    val description: String = ""// 수입/지출 상세 내용 (ex. 홍대역에서 합정역까지 택시로 이동
) : Parcelable



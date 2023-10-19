package com.nbcam_final_account_book.persentation.entry

import com.nbcam_final_account_book.data.model.remote.ResponseEntryModel
import java.time.LocalDateTime

data class EntryModel(
    val id: Long = 0, // 고유 식별자
    val type: String, // 수입 지출 구분 태그
    val dateTime: String,
    val value: String, // 수입/지출 값(ex. 10,000원)
    val tag: String, // ex. 교통비
    val title: String, // 수입/지출 제목 (ex. 택시)
    val description: String = ""// 수입/지출 상세 내용 (ex. 홍대역에서 합정역까지 택시로 이동
)

fun EntryModel.toResponse(key: String): ResponseEntryModel {
    return ResponseEntryModel(
        key = key,
        id = id,
        type = type,
        dateTime = dateTime,
        value = value,
        tag = tag,
        title = title,
        description = description
    )
}

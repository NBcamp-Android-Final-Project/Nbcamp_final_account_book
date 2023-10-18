package com.nbcam_final_account_book.data.model.remote

data class ResponseTagModel(
    val key: String, // firebase 고유 식별자
    val id: Int,
    val img: Int,
    val tagName: String
)

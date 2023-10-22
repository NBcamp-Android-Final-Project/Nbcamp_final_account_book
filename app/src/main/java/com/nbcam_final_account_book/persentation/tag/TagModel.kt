package com.nbcam_final_account_book.persentation.tag

import com.nbcam_final_account_book.data.model.remote.ResponseTagModel

data class TagModel(
    val id: Int = 0,
    val img: Int,
    val tagName: String
)

fun TagModel.toResponse(key: String): ResponseTagModel {
    return ResponseTagModel(
        key = key,
        id = id,
        img = img,
        tagName = tagName
    )
}
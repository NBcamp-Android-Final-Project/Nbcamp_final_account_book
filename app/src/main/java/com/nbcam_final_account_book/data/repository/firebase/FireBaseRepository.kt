package com.nbcam_final_account_book.data.repository.firebase

import com.nbcam_final_account_book.data.model.remote.ResponseEntryModel
import com.nbcam_final_account_book.data.model.remote.ResponseTagModel
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel

interface FireBaseRepository {

    suspend fun getUser(): String
    suspend fun getAllEntry(
        user: String,
        template: String,
        type: String
    ): List<ResponseEntryModel>

    suspend fun setEntry(
        user: String,
        template: String,
        type: String,
        item: EntryModel
    ): List<ResponseEntryModel>

    suspend fun deleteEntry(
        user: String,
        template: String,
        type: String,
        item: ResponseEntryModel
    ): List<ResponseEntryModel>

    suspend fun getAllTag(
        user: String,
        template: String,
        type: String
    ): List<ResponseTagModel>

    suspend fun setTag(
        user: String,
        template: String,
        type: String,
        item: TagModel
    ): List<ResponseTagModel>

    suspend fun deleteTag(
        user: String,
        template: String,
        type: String,
        item: ResponseTagModel
    ): List<ResponseTagModel>
}
package com.nbcam_final_account_book.data.repository.firebase

import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.remote.ResponseEntryModel
import com.nbcam_final_account_book.data.model.remote.ResponseTagModel
import com.nbcam_final_account_book.data.model.remote.ResponseTemplateModel
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel

interface FireBaseRepository {

    fun getUser(): String
    suspend fun getAllEntry(
        user: String,
        template: String,
        path: String
    ): List<ResponseEntryModel>

    suspend fun setEntry(
        user: String,
        template: String,
        path: String,
        item: EntryModel
    ): List<ResponseEntryModel>

    suspend fun deleteEntry(
        user: String,
        template: String,
        path: String,
        item: ResponseEntryModel
    ): List<ResponseEntryModel>

    suspend fun getAllTag(
        user: String,
        template: String,
        path: String
    ): List<ResponseTagModel>

    suspend fun setTag(
        user: String,
        template: String,
        path: String,
        item: TagModel
    ): List<ResponseTagModel>

    suspend fun deleteTag(
        user: String,
        template: String,
        path: String,
        item: ResponseTagModel
    ): List<ResponseTagModel>

    suspend fun getAllTemplate(user: String): List<ResponseTemplateModel>

    suspend fun setTemplate(user: String, item: TemplateEntity): List<ResponseTemplateModel>

    suspend fun setBudget(user: String, template: String, path: String)

    fun logout()
}
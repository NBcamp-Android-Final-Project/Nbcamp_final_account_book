package com.nbcam_final_account_book.data.repository.firebase

import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.remote.ResponseEntryModel
import com.nbcam_final_account_book.data.model.remote.ResponseTagModel
import com.nbcam_final_account_book.data.model.remote.ResponseTemplateModel
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel

interface FireBaseRepository {
    // template는 "${$item.templateTitle}-${item.id}" 형식으로 지정한다.
    // item은 TemplateEntity 형태다

    fun getUser(): String
    suspend fun getAllEntry(
        user: String,
        template: String,
    ): List<ResponseEntryModel>

    suspend fun setEntry(
        user: String,
        template: String,
        item: EntryModel
    ): List<ResponseEntryModel>

    suspend fun deleteEntry(
        user: String,
        template: String,
        item: ResponseEntryModel
    ): List<ResponseEntryModel>

    suspend fun getAllTag(
        user: String,
        template: String,
    ): List<ResponseTagModel>

    suspend fun setTag(
        user: String,
        template: String,
        item: TagModel
    ): List<ResponseTagModel>

    suspend fun deleteTag(
        user: String,
        template: String,
        item: ResponseTagModel
    ): List<ResponseTagModel>

    suspend fun getAllTemplate(user: String): List<ResponseTemplateModel>

    suspend fun setTemplate(user: String, item: TemplateEntity): List<ResponseTemplateModel>
    suspend fun setDeleteTemplate(
        user: String,
        template: String,
        path: String,
        templateItem: ResponseTemplateModel
    ): List<ResponseTemplateModel>

    suspend fun setBudget(user: String, template: String, budget: String)


    fun logout()
}
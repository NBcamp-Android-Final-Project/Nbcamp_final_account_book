package com.nbcam_final_account_book.data.repository.firebase

import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.remote.ResponseEntryModel
import com.nbcam_final_account_book.data.model.remote.ResponseTagModel
import com.nbcam_final_account_book.persentation.budget.BudgetModel
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel
import java.time.chrono.ThaiBuddhistEra

interface FireBaseRepository {
    // template는 "${$item.templateTitle}-${item.id}" 형식으로 지정한다.
    // item은 TemplateEntity 형태다

    //user
    fun getUser(): String

    fun logout()

    //tempalte

    suspend fun getAllTemplate(user: String): List<TemplateEntity>

    suspend fun setTemplate(user: String, item: TemplateEntity): List<TemplateEntity>

    //data
    suspend fun updateData(user: String, item: DataEntity)



}
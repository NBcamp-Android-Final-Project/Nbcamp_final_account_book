package com.nbcam_final_account_book.data.repository.room

import com.nbcam_final_account_book.data.model.local.TemplateEntity

interface RoomRepository {


    suspend fun getAllTemplate(): List<TemplateEntity>
    suspend fun insertFirstTemplate(text: String)
    suspend fun insertTemplate(item: TemplateEntity): List<TemplateEntity>
    suspend fun deleteTemplate(item: TemplateEntity): List<TemplateEntity>
    suspend fun deleteAllTemplate()
    suspend fun selectFirstTemplate() : TemplateEntity


}
package com.nbcam_final_account_book.data.repository.firebase


import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity


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
    suspend fun getBackupData(user: String): List<DataEntity>


}
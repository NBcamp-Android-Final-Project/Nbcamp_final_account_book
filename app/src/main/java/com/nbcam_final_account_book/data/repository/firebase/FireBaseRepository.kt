package com.nbcam_final_account_book.data.repository.firebase


import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.local.UserDataEntity


interface FireBaseRepository {
    // template는 "${$item.templateTitle}-${item.id}" 형식으로 지정한다.
    // item은 TemplateEntity 형태다

    //user
    fun getUser(): String

    fun logout()

    suspend fun updateUserInFireStore(user: UserDataEntity)
    suspend fun deleteUserInFireStore(email: String)
    suspend fun getUserInFireStore(uid: String) : UserDataEntity?


    //tempalte

    suspend fun getAllTemplate(user: String): List<TemplateEntity>

    suspend fun setTemplate(user: String, item: TemplateEntity): List<TemplateEntity>

    suspend fun setTemplateList(user: String, items: List<TemplateEntity>)

    suspend fun deleteTemplate(user: String, key: String)

    //data
    suspend fun updateData(user: String, item: DataEntity)
    suspend fun updateDataList(user: String, items: List<DataEntity>)
    suspend fun getBackupData(user: String): List<DataEntity>

    suspend fun deleteData(user: String, key: String)

}
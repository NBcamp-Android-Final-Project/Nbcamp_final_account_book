package com.nbcam_final_account_book.data.repository.room

import androidx.lifecycle.LiveData
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity

interface RoomRepository {


    //Template
    suspend fun getAllTemplate(): List<TemplateEntity>
    suspend fun insertFirstTemplate(text: String): String
    suspend fun insertTemplate(text: String): List<TemplateEntity>
    suspend fun deleteTemplate(item: TemplateEntity): List<TemplateEntity>
    suspend fun deleteAllTemplate()
    suspend fun selectFirstTemplate(key: String): TemplateEntity

    //Data
    suspend fun insertData(item: DataEntity)
    suspend fun getAllData(key: String): DataEntity?

    //Entry
    fun getAllEntry(): LiveData<List<EntryEntity>>
    suspend fun insertEntry(item: EntryEntity)
    suspend fun deleteAllEntry()

    //Budget
    suspend fun insertBudget(item:BudgetEntity)
    suspend fun deleteAllBudget()

    //Tag


}
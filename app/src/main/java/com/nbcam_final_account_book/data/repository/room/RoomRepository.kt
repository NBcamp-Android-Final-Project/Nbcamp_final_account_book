package com.nbcam_final_account_book.data.repository.room

import androidx.lifecycle.LiveData
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
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
    suspend fun deleteAllData()
    suspend fun updateData(item: DataEntity)

    //Entry
    fun getAllEntry(): LiveData<List<EntryEntity>>
    fun getEntryById(id: Int): EntryEntity
    fun getEntryByKey(key: String?): LiveData<List<EntryEntity>>
    suspend fun insertEntry(item: EntryEntity)
    suspend fun deleteAllEntry()
    suspend fun deleteEntry(id: Int)
    suspend fun deleteEntryByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터

    //Budget
    suspend fun insertBudget(item: BudgetEntity)
    fun getAllBudget(): LiveData<List<BudgetEntity>>
    fun getEBudgetById(id: Int): BudgetEntity
    fun getBudgetByKey(key: String?): LiveData<List<BudgetEntity>>

    suspend fun deleteBudgetById(id: Int)
    suspend fun deleteBudgetByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터
    suspend fun deleteAllBudget()
    suspend fun updateBudget(item: BudgetEntity)

    //Tag

    suspend fun insertTag(item: TagEntity)

    fun getAllTag(): LiveData<List<TagEntity>>//데이터 백업 시 반환되는 데이터
    fun getTagById(id: Int): TagEntity // 데이터 수정시 수정할 데이터
    fun getTagByKey(key: String?): LiveData<List<TagEntity>> //템플릿 선택 시 반환되는 데이터

    suspend fun deleteTagById(id: Int)
    suspend fun deleteTagByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터
    suspend fun deleteAllTag()

    suspend fun updateTag(item: TagEntity)

}
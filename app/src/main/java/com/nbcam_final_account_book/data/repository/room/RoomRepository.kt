package com.nbcam_final_account_book.data.repository.room

import androidx.lifecycle.LiveData
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity

interface RoomRepository {


    //Template
    fun getAllListTemplate(): List<TemplateEntity>
    fun getAllLiveTemplate(): LiveData<List<TemplateEntity>>
    suspend fun insertFirstTemplate(text: String): String
    suspend fun insertTemplate(text: String): List<TemplateEntity>
    suspend fun insertTemplateList(item:List<TemplateEntity>)
    suspend fun deleteTemplate(item: TemplateEntity): List<TemplateEntity>
    suspend fun deleteAllTemplate()
    suspend fun selectFirstTemplate(key: String): TemplateEntity

    //Data
    suspend fun insertData(item: DataEntity)
    suspend fun insertDataList(itemList: List<DataEntity>)
    suspend fun getDataByKey(key: String): DataEntity?
    suspend fun getAllData(): List<DataEntity>
    suspend fun deleteAllData()
    suspend fun updateData(item: DataEntity)

    //Entry
    fun getAllLiveEntry(): LiveData<List<EntryEntity>>
    fun getAllListEntry(): List<EntryEntity> //데이터 백업 시 반환되는 데이터

    fun getEntryById(id: Int): EntryEntity
    fun getLiveEntryByKey(key: String?): LiveData<List<EntryEntity>>
    fun getListEntryKey(key: String?): List<EntryEntity> //템플릿 선택 백업 시

    suspend fun insertEntry(item: EntryEntity)
    suspend fun insertEntryList(item: List<EntryEntity>)
    suspend fun deleteAllEntry()
    suspend fun deleteEntry(id: Int)
    suspend fun deleteEntryByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터


    //Budget
    suspend fun insertBudget(item: BudgetEntity)
    suspend fun insertBudgetList(item: List<BudgetEntity>)

    fun getAllLiveBudget(): LiveData<List<BudgetEntity>>
    fun getAllListBudget(): List<BudgetEntity> //데이터 백업 시 반환되는 데이터
    fun getEBudgetById(id: Int): BudgetEntity
    fun getLiveBudgetByKey(key: String?): LiveData<List<BudgetEntity>>
    fun getListBudgetByKey(key: String?): List<BudgetEntity> //템플릿 선택 백업 시

    suspend fun deleteBudgetById(id: Int)
    suspend fun deleteBudgetByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터
    suspend fun deleteAllBudget()
    suspend fun updateBudget(item: BudgetEntity)

    //Tag

    suspend fun insertTag(item: TagEntity)
    suspend fun insertTagList(item: List<TagEntity>)

    fun getAllLiveTag(): LiveData<List<TagEntity>>//데이터 백업 시 반환되는 데이터
    fun getAllListTag(): List<TagEntity> //데이터 백업 시 반환되는 데이터
    fun getTagById(id: Int): TagEntity // 데이터 수정시 수정할 데이터
    fun getLiveTagByKey(key: String?): LiveData<List<TagEntity>> //템플릿 선택 시 반환되는 데이터
    fun getListTagKey(key: String?): List<TagEntity> //템플릿 선택 백업 시


    suspend fun deleteTagById(id: Int)
    suspend fun deleteTagByKey(key: String?) // 템플릿 삭제 시 모두 삭제해야 하는 데이터
    suspend fun deleteAllTag()

    suspend fun updateTag(item: TagEntity)

}
package com.nbcam_final_account_book.data.repository.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.DeleteEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import java.util.UUID

class RoomRepositoryImpl(
    private val database: AndroidRoomDataBase?
) : RoomRepository {

    //template
    override fun getAllListTemplate(): List<TemplateEntity> {
        val dao = database?.templateDao() ?: throw IllegalStateException("getAllTemplate fail")

        return dao.getListTemplate()
    }

    override fun getAllLiveTemplate(): LiveData<List<TemplateEntity>> {
        val dao = database?.templateDao() ?: throw IllegalStateException("getAllLiveTemplate fail")

        return dao.getLiveTemplateList()
    }

    override suspend fun insertFirstTemplate(text: String): String {
        val dao = database?.templateDao() ?: throw IllegalStateException("insertFirstTemplate fail")
        val customKey = UUID.randomUUID().toString()

        dao.insertTemplate(TemplateEntity(id = customKey, templateTitle = text))
        return customKey
    }

    override suspend fun insertTemplate(text: String): List<TemplateEntity> {

        val dao = database?.templateDao() ?: throw IllegalStateException("insertTemplate fail")
        val customKey = UUID.randomUUID().toString()

        dao.insertTemplate(TemplateEntity(id = customKey, templateTitle = text))

        return dao.getListTemplate()
    }

    override suspend fun insertTemplateList(item: List<TemplateEntity>) {
        val dao = database?.templateDao() ?: throw IllegalStateException("insertTemplateList fail")

        dao.insertTemplateList(item)
    }

    override suspend fun deleteTemplate(item: TemplateEntity) {

        val dao = database?.templateDao() ?: throw IllegalStateException("deleteTemplate fail")

        dao.deleteTemplate(item.id)

    }

    override suspend fun deleteAllTemplate() {
        val dao = database?.templateDao() ?: throw IllegalStateException("deleteAllTemplate fail")

        dao.deleteAllTemplate()
    }

    override suspend fun selectTemplateByKey(key: String): TemplateEntity {
        val dao = database?.templateDao() ?: throw IllegalStateException("selectFirstTemplate fail")

        return dao.getFirstTemplate(key)
    }

    override suspend fun updateTemplate(item: TemplateEntity) {
        val dao = database?.templateDao() ?: throw IllegalStateException("updateTemplate fail")

        dao.updateTemplate(item)
    }

    // data

    override suspend fun insertData(item: DataEntity) {
        val dao = database?.dataDao() ?: throw IllegalStateException("insertData fail")

        dao.insertData(item)
    }

    override suspend fun insertDataList(itemList: List<DataEntity>) {
        val dao = database?.dataDao() ?: throw IllegalStateException("insertDataList fail")

        dao.insertDataList(itemList)
    }


    override suspend fun getDataByKey(key: String): DataEntity? {
        val dao = database?.dataDao() ?: throw IllegalStateException("getDataByKey fail")

        return dao.getDataById(key)
    }

    override suspend fun getAllData(): List<DataEntity> {
        val dao = database?.dataDao() ?: throw IllegalStateException("getAllData fail")

        return dao.getAllData()
    }

    override suspend fun deleteAllData() {
        val dao = database?.dataDao() ?: throw IllegalStateException("deleteAllData fail")
        dao.deleteAllData()
    }

    override suspend fun deleteDataByKey(id: String) {
        val dao = database?.dataDao() ?: throw IllegalStateException("deleteDataByKey fail")
        dao.deleteDataById(id)
    }

    override suspend fun updateData(item: DataEntity) {
        val dao = database?.dataDao() ?: throw IllegalStateException("updateData fail")

        dao.updateData(item)
    }

    //Entry
    override fun getAllLiveEntry(): LiveData<List<EntryEntity>> {
        val dao = database?.entryDao() ?: throw IllegalStateException("getAllEntry fail")
        return dao.getAllLiveEntry()
    }

    override fun getAllListEntry(): List<EntryEntity> {
        val dao = database?.entryDao() ?: throw IllegalStateException("getAllListEntry fail")
        return dao.getAllListEntry()
    }

    override fun getEntryById(id: Int): EntryEntity {
        val dao = database?.entryDao() ?: throw IllegalStateException("getEntryById fail")
        return dao.getEntryById(id)
    }

    override fun getLiveEntryByKey(key: String?): LiveData<List<EntryEntity>> {

        val dao = database?.entryDao() ?: throw IllegalStateException("getLiveEntryByKey fail")
        Log.d("삽입키", key.toString())
        return if (key != null) {
            dao.getLiveEntryByKey(key)
        } else {
            Log.e("룸오류", "getLiveEntryByKey 오류")
            MutableLiveData<List<EntryEntity>>()
        }
    }

    override fun getListEntryKey(key: String?): List<EntryEntity> {
        val dao = database?.entryDao() ?: throw IllegalStateException("getListEntryKey fail")
        Log.d("삽입키", key.toString())
        return if (key != null) {
            dao.getListEntryByKey(key)
        } else {
            Log.e("룸오류", "getListEntryKey 오류")
            emptyList()
        }
    }

    override suspend fun insertEntry(item: EntryEntity) {
        val dao = database?.entryDao() ?: throw IllegalStateException("insertEntry fail")
        dao.insertEntry(item)
    }

    override suspend fun insertEntryList(item: List<EntryEntity>) {
        val dao = database?.entryDao() ?: throw IllegalStateException("insertEntryList fail")
        dao.insertEntryList(item)
    }

    override suspend fun deleteAllEntry() {
        val dao = database?.entryDao() ?: throw IllegalStateException("deleteAllEntry fail")
        dao.deleteAllEntry()
    }

    override suspend fun deleteEntry(id: Int) {
        val dao = database?.entryDao() ?: throw IllegalStateException("deleteEntry fail")
        dao.deleteEntryById(id)
    }

    override suspend fun deleteEntryByKey(key: String?) {
        val dao = database?.entryDao() ?: throw IllegalStateException("deleteEntryByKey fail")

        if (key != null) {
            dao.deleteEntryByKey(key)
        } else {
            Log.e("룸오류", "deleteEntryByKey: 오류")
        }

    }

    override suspend fun updateEntry(item: EntryEntity) {
        val dao = database?.entryDao() ?: throw IllegalStateException("updateEntry fail")

        dao.updateEntry(item)
    }

    //Budget
    override suspend fun insertBudget(item: BudgetEntity) {
        val dao = database?.budgetDao() ?: throw IllegalStateException("insertBudget fail")

        dao.insertBudget(item)
    }

    override suspend fun insertBudgetList(item: List<BudgetEntity>) {
        val dao = database?.budgetDao() ?: throw IllegalStateException("insertBudgetList fail")

        dao.insertBudgetList(item)
    }

    override fun getAllLiveBudget(): LiveData<List<BudgetEntity>> {
        val dao = database?.budgetDao() ?: throw IllegalStateException("getAllBudget fail")

        return dao.getAllLiveBudget()
    }

    override fun getAllListBudget(): List<BudgetEntity> {
        val dao = database?.budgetDao() ?: throw IllegalStateException("getAllListBudget fail")
        return dao.getAllListBudget()
    }

    override fun getEBudgetById(id: Int): BudgetEntity {
        val dao = database?.budgetDao() ?: throw IllegalStateException("getEBudgetById fail")

        return dao.getEBudgetById(id)
    }

    override fun getLiveBudgetByKey(key: String?): LiveData<List<BudgetEntity>> {

        val dao = database?.budgetDao() ?: throw IllegalStateException("getLiveBudgetByKey fail")
        return if (key != null) {
            dao.getLiveBudgetByKey(key)
        } else {
            Log.e("룸오류", "getLiveBudgetByKey: 오류")
            MutableLiveData<List<BudgetEntity>>()
        }
    }

    override fun getListBudgetByKey(key: String?): List<BudgetEntity> {
        val dao = database?.budgetDao() ?: throw IllegalStateException("getListBudgetByKey fail")
        return if (key != null) {
            dao.getListBudgetByKey(key)
        } else {
            Log.e("룸오류", "getListBudgetByKey: 오류")
            emptyList()
        }
    }

    override suspend fun deleteBudgetById(id: Int) {
        val dao = database?.budgetDao() ?: throw IllegalStateException("deleteBudgetById fail")
        dao.deleteBudgetById(id)
    }

    override suspend fun deleteBudgetByKey(key: String?) {
        val dao = database?.budgetDao() ?: throw IllegalStateException("deleteBudgetByKey fail")
        if (key != null) {
            dao.deleteBudgetByKey(key)
        }

    }

    override suspend fun deleteAllBudget() {
        val dao = database?.budgetDao() ?: throw IllegalStateException("deleteAllBudget fail")

        dao.deleteAllBudget()
    }

    override suspend fun updateBudget(item: BudgetEntity) {
        val dao = database?.budgetDao() ?: throw IllegalStateException("updateBudget fail")

        dao.updateBudget(item)
    }

    //Tag
    override suspend fun insertTag(item: TagEntity) {
        val dao = database?.tagDao() ?: throw IllegalStateException("insertTag fail")
        dao.insertTag(item)
    }

    override suspend fun insertTagList(item: List<TagEntity>) {
        val dao = database?.tagDao() ?: throw IllegalStateException("insertTagList fail")
        Log.d("호출.리스트", item.toString())
        dao.insertTagList(item)
    }

    override fun getAllLiveTag(): LiveData<List<TagEntity>> {
        val dao = database?.tagDao() ?: throw IllegalStateException("getAllTag fail")
        return dao.getAllLiveTag()
    }

    override fun getAllListTag(): List<TagEntity> {
        val dao = database?.tagDao() ?: throw IllegalStateException("getAllListTag fail")
        return dao.getAllListTag()
    }

    override fun getTagById(id: Long): TagEntity {
        val dao = database?.tagDao() ?: throw IllegalStateException("getTagById fail")
        return dao.getTagById(id)
    }

    override fun getLiveTagByKey(key: String?): LiveData<List<TagEntity>> {
        val dao = database?.tagDao() ?: throw IllegalStateException("getLiveTagByKey fail")
        return if (key != null) {
            dao.getLiveTagByKey(key)
        } else {
            Log.e("룸오류", "getLiveTagByKey: 에러 ")
            MutableLiveData<List<TagEntity>>()
        }
    }

    override fun getListTagKey(key: String?): List<TagEntity> {
        val dao = database?.tagDao() ?: throw IllegalStateException("getListTagKey fail")
        return if (key != null) {
            dao.getListTagByKey(key)
        } else {
            Log.e("룸오류", "getListTagKey: 오류")
            emptyList()
        }
    }

    override suspend fun deleteTagById(id: Long) {
        val dao = database?.tagDao() ?: throw IllegalStateException("deleteTag fail")
        dao.deleteTagById(id)
    }

    override suspend fun deleteTagByKey(key: String?) {
        val dao = database?.tagDao() ?: throw IllegalStateException("deleteTagByKey fail")
        if (key != null) {
            dao.deleteTagByKey(key)
        } else {
            Log.e("룸오류", "deleteTagByKey: key 오류")
        }
    }

    override suspend fun deleteAllTag() {
        val dao = database?.tagDao() ?: throw IllegalStateException("deleteAllTag fail")
        dao.deleteAllTag()
    }

    override suspend fun updateTag(item: TagEntity) {
        val dao = database?.tagDao() ?: throw IllegalStateException("updateTag fail")

        dao.updateTag(item)
    }

    //DeleteEntity

    override suspend fun deleteAllDeleteEntity() {
        val dao = database?.deleteDao() ?: throw IllegalStateException("deleteAllDeleteEntity fail")

        dao.deleteAllDeleteEntity()
    }

    override suspend fun insertDelete(item: DeleteEntity) {
        val dao = database?.deleteDao() ?: throw IllegalStateException("insertDelete fail")

        dao.insertDelete(item)
    }

    override suspend fun getAllDelete(): List<DeleteEntity> {
        val dao = database?.deleteDao() ?: throw IllegalStateException("getAllDelete fail")

        return dao.getAllDelete()
    }


}
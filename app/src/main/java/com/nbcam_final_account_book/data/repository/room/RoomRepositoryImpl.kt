package com.nbcam_final_account_book.data.repository.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import java.util.UUID

class RoomRepositoryImpl(
    private val database: AndroidRoomDataBase?
) : RoomRepository {

    //template
    override suspend fun getAllTemplate(): List<TemplateEntity> {
        val dao = database?.templateDao() ?: throw IllegalStateException("getAllTemplate fail")

        return dao.getTemplateList()
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

        return dao.getTemplateList()
    }

    override suspend fun deleteTemplate(item: TemplateEntity): List<TemplateEntity> {

        val dao = database?.templateDao() ?: throw IllegalStateException("deleteTemplate fail")

        dao.deleteTemplate(item.id)

        return dao.getTemplateList()
    }

    override suspend fun deleteAllTemplate() {
        val dao = database?.templateDao() ?: throw IllegalStateException("deleteAllTemplate fail")

        dao.deleteAllTemplate()
    }

    override suspend fun selectFirstTemplate(key: String): TemplateEntity {
        val dao = database?.templateDao() ?: throw IllegalStateException("selectFirstTemplate fail")

        return dao.getFirstTemplate(key)
    }

    // data

    override suspend fun insertData(item: DataEntity) {
        val dao = database?.dataDao() ?: throw IllegalStateException("insertData fail")

        dao.insertData(item)
    }


    override suspend fun getAllData(key: String): DataEntity? {
        val dao = database?.dataDao() ?: throw IllegalStateException("getAllData fail")

        return dao.getDataById(key)
    }

    override suspend fun deleteAllData() {
        val dao = database?.dataDao() ?: throw IllegalStateException("deleteAllData fail")
        dao.deleteAllData()
    }

    override suspend fun updateData(item: DataEntity) {
        val dao = database?.dataDao() ?: throw IllegalStateException("updateData fail")

        dao.updateData(item)
    }

    //Entry
    override fun getAllEntry(): LiveData<List<EntryEntity>> {
        val dao = database?.entryDao() ?: throw IllegalStateException("getAllEntry fail")
        return dao.getAllEntry()
    }

    override fun getEntryById(id: Int): EntryEntity {
        val dao = database?.entryDao() ?: throw IllegalStateException("getEntryById fail")
        return dao.getEntryById(id)
    }

    override fun getEntryByKey(key: String?): LiveData<List<EntryEntity>> {

        val dao = database?.entryDao() ?: throw IllegalStateException("getEntryByKey fail")
        return if (key != null) {
            dao.getEntryByKey(key)
        } else {
            Log.e("룸오류", "getEntryByKey 오류")
            MutableLiveData<List<EntryEntity>>()
        }
    }

    override suspend fun insertEntry(item: EntryEntity) {
        val dao = database?.entryDao() ?: throw IllegalStateException("insertEntry fail")
        dao.insertEntry(item)
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

    //Budget
    override suspend fun insertBudget(item: BudgetEntity) {
        val dao = database?.budgetDao() ?: throw IllegalStateException("insertBudget fail")

        dao.insertBudget(item)
    }

    override fun getAllBudget(): LiveData<List<BudgetEntity>> {
        val dao = database?.budgetDao() ?: throw IllegalStateException("getAllBudget fail")

        return dao.getAllBudget()
    }

    override fun getEBudgetById(id: Int): BudgetEntity {
        val dao = database?.budgetDao() ?: throw IllegalStateException("getEBudgetById fail")

        return dao.getEBudgetById(id)
    }

    override fun getBudgetByKey(key: String?): LiveData<List<BudgetEntity>> {

        val dao = database?.budgetDao() ?: throw IllegalStateException("insertBudget fail")
        return if (key != null) {
            dao.getBudgetByKey(key)
        } else {
            Log.e("룸오류", "getBudgetByKey: 오류")
            MutableLiveData<List<BudgetEntity>>()
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

    override fun getAllTag(): LiveData<List<TagEntity>> {
        val dao = database?.tagDao() ?: throw IllegalStateException("getAllTag fail")
        return dao.getAllTag()
    }

    override fun getTagById(id: Int): TagEntity {
        val dao = database?.tagDao() ?: throw IllegalStateException("getTagById fail")
        return dao.getTagById(id)
    }

    override fun getTagByKey(key: String?): LiveData<List<TagEntity>> {
        val dao = database?.tagDao() ?: throw IllegalStateException("getTagByKey fail")
        return if (key != null) {
            dao.getTagByKey(key)
        } else {
            Log.e("룸오류", "getTagByKey: 에러 ")
            MutableLiveData<List<TagEntity>>()
        }
    }

    override suspend fun deleteTagById(id: Int) {
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
        dao.getAllTag()
    }

    override suspend fun updateTag(item: TagEntity) {
        val dao = database?.tagDao() ?: throw IllegalStateException("updateTag fail")

        dao.updateTag(item)
    }


}
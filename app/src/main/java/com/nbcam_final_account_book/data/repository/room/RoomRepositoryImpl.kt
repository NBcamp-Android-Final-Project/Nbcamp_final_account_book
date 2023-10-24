package com.nbcam_final_account_book.data.repository.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
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

    //Entry
    override fun getAllEntry(): LiveData<List<EntryEntity>> {
        val dao = database?.entryDao() ?: throw IllegalStateException("getAllEntry fail")
        return dao.getAllEntry()
    }

    override fun getEntryByKey(key: String?): LiveData<List<EntryEntity>> {

        val dao = database?.entryDao() ?: throw IllegalStateException("getAllEntry fail")
        return if (key != null) {
            dao.getEntryByKey(key)
        } else {
            Log.e("getEntryByKey", "getEntryByKey 오류" )
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

    //Budget
    override suspend fun insertBudget(item: BudgetEntity) {
        val dao = database?.budgetDao() ?: throw IllegalStateException("insertBudget fail")

        dao.insertBudget(item)
    }

    override suspend fun deleteAllBudget() {
        val dao = database?.budgetDao() ?: throw IllegalStateException("deleteAllBudget fail")

        dao.deleteAllBudget()
    }

    //Tag
    override suspend fun deleteAllTag() {
        val dao = database?.tagDao() ?: throw IllegalStateException("deleteAllTag fal")
    }


}
package com.nbcam_final_account_book.data.repository.room

import androidx.lifecycle.LiveData
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import java.util.UUID

class RoomRepositoryImpl(
    private val database: AndroidRoomDataBase?
) : RoomRepository {
    override suspend fun getAllTemplate(): List<TemplateEntity> {
        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        return dao.getTemplateList()
    }

    override suspend fun insertFirstTemplate(text: String): String {
        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")
        val customKey = UUID.randomUUID().toString()

        dao.insertTemplate(TemplateEntity(id = customKey, templateTitle = text))
        return customKey
    }

    override suspend fun insertTemplate(text: String): List<TemplateEntity> {

        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")
        val customKey = UUID.randomUUID().toString()

        dao.insertTemplate(TemplateEntity(id = customKey, templateTitle = text))

        return dao.getTemplateList()
    }

    override suspend fun deleteTemplate(item: TemplateEntity): List<TemplateEntity> {

        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        dao.deleteTemplate(item.id)

        return dao.getTemplateList()
    }

    override suspend fun deleteAllTemplate() {
        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        dao.deleteAllTemplate()
    }

    override suspend fun selectFirstTemplate(key: String): TemplateEntity {
        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        return dao.getFirstTemplate(key)
    }

    override suspend fun insertData(item: DataEntity) {
        val dao = database?.dataDao() ?: throw IllegalStateException("test fail")

        dao.insertData(item)
    }


    override suspend fun getAllData(key: String): DataEntity? {
        val dao = database?.dataDao() ?: throw IllegalStateException("test fail")

        return dao.getDataById(key)
    }

    //Entry
    override fun getAllEntry(): LiveData<List<EntryEntity>> {
        val dao = database?.entryDao() ?: throw IllegalStateException("test fail")
        return dao.getAllEntry()
    }

    override suspend fun insertEntry(item: EntryEntity) {
        val dao = database?.entryDao() ?: throw IllegalStateException("test fail")

        dao.insertEntry(item)
    }


}
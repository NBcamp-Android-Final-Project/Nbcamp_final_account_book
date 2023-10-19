package com.nbcam_final_account_book.data.repository.room

import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase

class RoomRepositoryImpl(
    private val database: AndroidRoomDataBase?
) : RoomRepository {
    override suspend fun getAllTemplate(): List<TemplateEntity> {
        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        return dao.getTemplateList()
    }

    override suspend fun insertFirstTemplate(text: String) {
        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        dao.insertTemplate(TemplateEntity(id = 0, templateTitle = text))
    }

    override suspend fun insertTemplate(item: TemplateEntity): List<TemplateEntity> {

        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        dao.insertTemplate(item)

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

    override suspend fun selectFirstTemplate(): TemplateEntity {
        val dao = database?.templateDao() ?: throw IllegalStateException("test fail")

        return dao.getFirstTemplate()
    }


}
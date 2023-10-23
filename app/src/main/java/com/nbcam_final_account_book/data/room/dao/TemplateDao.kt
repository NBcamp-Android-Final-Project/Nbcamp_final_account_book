package com.nbcam_final_account_book.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.TemplateEntity

@Dao
interface TemplateDao {

    @Query("SELECT * FROM template_table")
    suspend fun getTemplateList(): List<TemplateEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTemplate(item: TemplateEntity)

    @Update
    suspend fun updateTemplate(item: TemplateEntity)

    @Query("DELETE FROM template_table WHERE id = :id")
    suspend fun deleteTemplate(id: String)

    @Query("DELETE FROM template_table")
    suspend fun deleteAllTemplate() // 메서드 이름 변경

    @Query("SELECT * FROM template_table WHERE id = :id")
    suspend fun getFirstTemplate(id: String): TemplateEntity


}
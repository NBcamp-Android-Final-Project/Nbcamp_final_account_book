package com.nbcam_final_account_book.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nbcam_final_account_book.data.model.local.TemplateEntity

@Dao
interface TemplateDao {

    @Query("SELECT * FROM template_table")
    suspend fun getTemplateList(): List<TemplateEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTemplate(item: TemplateEntity)

    @Query("DELETE FROM template_table WHERE id = :id")
    suspend fun deleteTemplate(id: Int)

}
package com.nbcam_final_account_book.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.TemplateEntity

@Dao
interface TemplateDao {

    //SELECT
    @Query("SELECT * FROM template_table")
    fun getListTemplate(): List<TemplateEntity>

    @Query("SELECT * FROM template_table")
    fun getLiveTemplateList(): LiveData<List<TemplateEntity>>

    @Query("SELECT * FROM template_table WHERE template_id = :id")
    suspend fun getFirstTemplate(id: String): TemplateEntity

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTemplate(item: TemplateEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTemplateList(item: List<TemplateEntity>)


    //DELETE
    @Query("DELETE FROM template_table WHERE template_id = :id")
    suspend fun deleteTemplate(id: String)

    @Query("DELETE FROM template_table")
    suspend fun deleteAllTemplate()

    //Update
    @Update
    suspend fun updateTemplate(item: TemplateEntity)


}
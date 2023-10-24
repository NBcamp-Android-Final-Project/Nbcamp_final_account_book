package com.nbcam_final_account_book.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity

@Dao
interface DataDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(entity: DataEntity) // DataEntity.id = TemplateEntity.id

    //SELECT
    @Query("SELECT * FROM data_table WHERE data_id = :id")
    fun getDataById(id: String): DataEntity? // id = TemplateEntity.id

    //DELETE
    @Query("DELETE FROM data_table WHERE data_id = :id")
    fun deleteDataById(id: Int) // id = TemplateEntity.id

    //Update
    @Update
    suspend fun updateData(entity: DataEntity)



}
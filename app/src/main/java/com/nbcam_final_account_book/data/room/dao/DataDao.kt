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

    @Query("SELECT * FROM data_table WHERE data_id = :id")
    suspend fun getDataById(id: String): DataEntity? // id = TemplateEntity.id

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(entity: DataEntity) // DataEntity.id = TemplateEntity.id

    @Update
    suspend fun updateData(entity: DataEntity)

    @Query("DELETE FROM data_table WHERE data_id = :id")
    suspend fun deleteDataById(id: Int) // id = TemplateEntity.id

}
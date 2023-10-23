package com.nbcam_final_account_book.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.DataEntity

@Dao
interface DataDao {

    @Query("SELECT * FROM data_table WHERE data_id = :id")
    suspend fun dataFindById(id: Int): DataEntity?
    @Insert
    suspend fun insertData(entity: DataEntity)
    @Update
    suspend fun updateData(entity: DataEntity)

}
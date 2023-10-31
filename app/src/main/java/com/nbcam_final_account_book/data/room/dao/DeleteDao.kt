package com.nbcam_final_account_book.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nbcam_final_account_book.data.model.local.DeleteEntity

@Dao
interface DeleteDao {

    @Query("SELECT * FROM delete_table")
    fun getAllDelete(): List<DeleteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDelete(item: DeleteEntity)

    @Query("DELETE FROM delete_table")
    suspend fun deleteAllDeleteEntity()
}
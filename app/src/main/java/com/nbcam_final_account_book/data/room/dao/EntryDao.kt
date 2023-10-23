package com.nbcam_final_account_book.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nbcam_final_account_book.data.model.local.EntryEntity

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry_table")
    fun getAllEntry(): LiveData<List<EntryEntity>>

    @Insert
    suspend fun insertEntry(item: EntryEntity)

    @Query("SELECT * FROM entry_table WHERE entry_id = :id")
    suspend fun getEntryById(id: Int): EntryEntity

    @Query("DELETE FROM entry_table WHERE entry_id = :id")
    suspend fun deleteItem(id: Int)

}
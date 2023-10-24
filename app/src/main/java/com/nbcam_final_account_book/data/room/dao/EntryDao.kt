package com.nbcam_final_account_book.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.EntryEntity

@Dao
interface EntryDao {

    //SELECT
    @Query("SELECT * FROM entry_table")
    fun getAllEntry(): LiveData<List<EntryEntity>>

    @Query("SELECT * FROM entry_table WHERE entry_id = :id")
    suspend fun getEntryById(id: Int): EntryEntity

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntry(item: EntryEntity)

    //DELETE

    @Query("DELETE FROM entry_table WHERE entry_id = :id")
    suspend fun deleteEntry(id: Int)

    @Query("DELETE FROM entry_table WHERE entry_key = :key")
    suspend fun deleteEntryByKey(key: String) // 템플릿 삭제 시 모두 삭제해야 하는 데이터

    @Query("DELETE FROM entry_table")
    suspend fun deleteAllEntry() // 모든 테이블 삭제

    //Update
    @Update
    suspend fun updateEntry(item: EntryEntity)

}
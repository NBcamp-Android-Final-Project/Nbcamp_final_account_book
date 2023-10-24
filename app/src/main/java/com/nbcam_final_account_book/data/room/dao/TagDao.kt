package com.nbcam_final_account_book.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.TagEntity

@Dao
interface TagDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(item: TagEntity)

    //SELECT
    @Query("SELECT * FROM tag_table")
    fun getAllTag(): LiveData<List<TagEntity>>//데이터 백업 시 반환되는 데이터

    @Query("SELECT * FROM tag_table WHERE tag_id = :id")
    suspend fun getTagById(id: Int): TagEntity // 데이터 수정시 수정할 데이터

    @Query("SELECT * FROM tag_table WHERE tag_key = :key")
    suspend fun getTagByKey(key: String): LiveData<List<TagEntity>> //템플릿 선택 시 반환되는 데이터


    //DELETE

    @Query("DELETE FROM tag_table WHERE tag_id = :id")
    suspend fun deleteTag(id: Int)

    @Query("DELETE FROM tag_table WHERE tag_key = :key")
    suspend fun deleteTagByKey(key: String) // 템플릿 삭제 시 모두 삭제해야 하는 데이터

    @Query("DELETE FROM tag_table")
    suspend fun deleteAllTag() // 모든 테이블 삭제

    //Update
    @Update
    suspend fun updateTag(item: TagEntity)
}
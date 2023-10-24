package com.nbcam_final_account_book.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity

@Dao
interface BudgetDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudget(item: BudgetEntity)

    //SELECT
    @Query("SELECT * FROM budget_table")
    fun getAllBudget(): LiveData<List<BudgetEntity>> //데이터 백업 시 반환되는 데이터

    @Query("SELECT * FROM budget_table WHERE budget_id = :id")
    suspend fun getEBudgetById(id: Int): BudgetEntity // 데이터 수정시 수정할 데이터

    @Query("SELECT * FROM budget_table WHERE budget_key = :key")
    suspend fun getBudgetByKey(key: String): LiveData<List<BudgetEntity>> //템플릿 선택 시 반환되는 데이터


    //DELETE

    @Query("DELETE FROM budget_table WHERE budget_id = :id")
    suspend fun deleteBudget(id: Int)

    @Query("DELETE FROM budget_table WHERE budget_key = :key")
    suspend fun deleteBudgetByKey(key: String) // 템플릿 삭제 시 모두 삭제해야 하는 데이터

    @Query("DELETE FROM budget_table")
    suspend fun deleteAllBudget() // 모든 테이블 삭제

    //Update
    @Update
    suspend fun updateBudget(item: BudgetEntity)
}
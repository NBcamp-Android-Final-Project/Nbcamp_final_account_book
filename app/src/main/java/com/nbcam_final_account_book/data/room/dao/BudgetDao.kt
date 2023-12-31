package com.nbcam_final_account_book.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.BudgetEntity

@Dao
interface BudgetDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudget(item: BudgetEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudgetList(item: List<BudgetEntity>)

    //SELECT
    @Query("SELECT * FROM budget_table")
    fun getAllLiveBudget(): LiveData<List<BudgetEntity>> //데이터 백업 시 반환되는 데이터

    @Query("SELECT * FROM budget_table")
    fun getAllListBudget(): List<BudgetEntity> //데이터 백업 시 반환되는 데이터

    @Query("SELECT * FROM budget_table WHERE budget_id = :id")
    fun getEBudgetById(id: Int): BudgetEntity // 데이터 수정시 수정할 데이터

    @Query("SELECT * FROM budget_table WHERE budget_key = :key")
    fun getLiveBudgetByKey(key: String): LiveData<List<BudgetEntity>> //템플릿 선택 시 반환되는 데이터
    @Query("SELECT * FROM budget_table WHERE budget_key = :key")
    fun getListBudgetByKey(key: String): List<BudgetEntity> //템플릿 선택 백업 시


    //DELETE

    @Query("DELETE FROM budget_table WHERE budget_id = :id")
    suspend fun deleteBudgetById(id: Int)

    @Query("DELETE FROM budget_table WHERE budget_key = :key")
    suspend fun deleteBudgetByKey(key: String) // 템플릿 삭제 시 모두 삭제해야 하는 데이터

    @Query("DELETE FROM budget_table")
    suspend fun deleteAllBudget() // 모든 테이블 삭제

    //Update
    @Update
    suspend fun updateBudget(item: BudgetEntity)
}
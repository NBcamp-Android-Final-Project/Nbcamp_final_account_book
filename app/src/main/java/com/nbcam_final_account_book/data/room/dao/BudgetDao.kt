package com.nbcam_final_account_book.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.nbcam_final_account_book.data.model.local.BudgetEntity

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudget(item: BudgetEntity)
}
package com.nbcam_final_account_book.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.UserDataEntity

@Dao
interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserData(user: UserDataEntity)

    @Query("DELETE FROM user_data_table WHERE user_uid = :key")
    suspend fun deleteUser(key: String)

    @Update
    suspend fun updateUser(user: UserDataEntity)
}
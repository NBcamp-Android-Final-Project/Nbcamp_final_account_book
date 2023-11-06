package com.nbcam_final_account_book.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nbcam_final_account_book.data.model.local.UserDataEntity

@Dao
interface UserDataDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserData(user: UserDataEntity)

    //SELECT
    @Query("SELECT * FROM user_data_table")
    fun getALlUserList(): List<UserDataEntity>

    @Query("SELECT * FROM user_data_table")
    fun getALlUserLiveData(): LiveData<List<UserDataEntity>>

    @Query("SELECT * FROM user_data_table WHERE user_uid = :key")
    suspend fun getUserByKey(key: String): UserDataEntity

    //DELETE
    @Query("DELETE FROM user_data_table WHERE user_uid = :key")
    suspend fun deleteUser(key: String)

    //Update
    @Update
    suspend fun updateUser(user: UserDataEntity)
}
package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class UserDataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_uid")
    val key: String = "",
    @ColumnInfo(name = "user_name")
    val name: String = "",
    @ColumnInfo(name = "user_id")
    val id: String = "",
    @ColumnInfo(name = "user_img")
    var img: String = ""
)
package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delete_table")
data class DeleteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "delete_key")
    val key: String
)

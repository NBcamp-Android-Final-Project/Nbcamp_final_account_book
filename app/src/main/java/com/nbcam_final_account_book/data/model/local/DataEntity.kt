package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "data_id")
    val id: Int = 0,
    @ColumnInfo("data_entryList")
    val entryList: String = "",
    @ColumnInfo(name = "data_tagList")
    val tagList: String = "",
    @ColumnInfo(name = "data_budgetList")
    val budgetList: String = ""
)

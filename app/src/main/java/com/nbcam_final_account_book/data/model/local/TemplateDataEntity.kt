package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "template_data_table")
data class TemplateDataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int, // 해당 TemplateEntity와 id를 일치 시켜서 동일한 데이터를 받을 수 있도록.
    @ColumnInfo(name = "entry")
    val entry: String,
    @ColumnInfo(name = "entry")
    val tag: String,
    @ColumnInfo(name = "budget")
    val budget: String

)
package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "template_table")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String = "",
    @ColumnInfo(name = "template")
    val templateTitle: String = ""
)



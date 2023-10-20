package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nbcam_final_account_book.data.model.remote.ResponseTemplateModel

@Entity(tableName = "template_table")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "key")
    val key: String = "",
    @ColumnInfo(name = "template")
    val templateTitle: String = ""
)

fun TemplateEntity.toResponse(key: String): ResponseTemplateModel {

    return ResponseTemplateModel(
        key = key,
        id = id,
        templateTitle = templateTitle
    )
}

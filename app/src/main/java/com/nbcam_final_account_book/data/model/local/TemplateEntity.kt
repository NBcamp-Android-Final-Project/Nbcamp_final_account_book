package com.nbcam_final_account_book.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nbcam_final_account_book.unit.Unit.TEMPLATE_LOCAL
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "template_table")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "template_id")
    val id: String = "",
    @ColumnInfo(name = "template_name")
    val templateTitle: String = "",
    @ColumnInfo(name = "template_user")
    val user: String = "",
    @ColumnInfo(name = "template_type")
    val type: String = TEMPLATE_LOCAL
) : Parcelable



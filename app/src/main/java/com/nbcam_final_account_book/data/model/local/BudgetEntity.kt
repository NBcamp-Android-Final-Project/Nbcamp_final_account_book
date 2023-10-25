package com.nbcam_final_account_book.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "budget_table")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    val id: Int = 0,
    @ColumnInfo(name = "budget_key")
    val key: String,
    @ColumnInfo(name = "budget_asset_type")
    val assetType: String = "",
    @ColumnInfo(name = "budget_value")
    val value: String = ""
) : Parcelable

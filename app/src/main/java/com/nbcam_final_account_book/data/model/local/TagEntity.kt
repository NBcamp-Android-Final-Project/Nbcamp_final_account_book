package com.nbcam_final_account_book.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tag_table")
data class TagEntity(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "tag_id")
	val id: Long,
	@ColumnInfo(name = "tag_key")
	val key: String,
	@ColumnInfo(name = "tag_img")
	val img: Int = 0,
	@ColumnInfo(name = "tag_title")
	val title: String,
	@ColumnInfo(name = "tag_order")
	val order: Int = 1
) : Parcelable

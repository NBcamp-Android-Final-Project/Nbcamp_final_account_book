package com.nbcam_final_account_book.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry_table")
data class EntryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    val id: Long = 0,
    @ColumnInfo(name = "entry_key")
    val key: String = "",
    @ColumnInfo(name = "entry_type")
    val type: String, // 수입 지출 구분 태그,
    @ColumnInfo(name = "entry_asset_type")
    val assetType: String = "",
    @ColumnInfo(name = "entry_dateTime")
    val dateTime: String = "",
    @ColumnInfo(name = "entry_value")
    val value: String = "", // 수입/지출 값(ex. 10,000원)
    @ColumnInfo(name = "entry_Tag")
    val tag: String = "", // ex. 교통비
    @ColumnInfo(name = "entry_title")
    val title: String = "", // 수입/지출 제목 (ex. 택시)
    @ColumnInfo(name = "entry_description")
    val description: String = ""// 수입/지출 상세 내용 (ex. 홍대역에서 합정역까지 택시로 이동

)

package com.nbcam_final_account_book.data.model

import androidx.lifecycle.MutableLiveData
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel
import com.nbcam_final_account_book.unit.Unit.TYPE_INCOME
import com.nbcam_final_account_book.unit.Unit.TYPE_PAY

object DummyData {

    val liveDummyEntry: MutableLiveData<List<EntryModel>> = MutableLiveData()
    val liveDummyTag: MutableLiveData<List<TagModel>> = MutableLiveData()

    init {
        liveDummyEntry.value = arrayListOf<EntryModel>().apply {
            for (i in 0..9) {
                add(
                    EntryModel(
                        id = i.toLong(),
                        type = TYPE_INCOME,
                        year = "2023",
                        month = "10",
                        day = "14",
                        value = "100000 +${i * 1000}",
                        tag = "",
                        title = "월급",
                    )
                )
            }
            for (i in 0..9) {
                add(
                    EntryModel(
                        id = i.toLong(),
                        type = TYPE_PAY,
                        year = "2023",
                        month = "10",
                        day = "14",
                        value = "1000 +${i * 1000}",
                        tag = "",
                        title = "포카칩",
                    )
                )
            }
        }
        liveDummyTag.value = arrayListOf<TagModel>().apply {
            for (i in 0..9) {
                add(
                    TagModel(
                        id = i,
                        img = i,
                        tagName = "TAG $i"
                    )
                )
            }
        }
    }

}
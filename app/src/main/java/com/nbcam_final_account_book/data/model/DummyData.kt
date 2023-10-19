package com.nbcam_final_account_book.data.model

import androidx.lifecycle.MutableLiveData
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_INCOME
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_PAY

object DummyData {

    val liveDummyEntry: MutableLiveData<List<EntryModel>> = MutableLiveData()
    val liveDummyTag: MutableLiveData<List<TagModel>> = MutableLiveData()

    var currentTemplate : String = "template"

    init {
        liveDummyEntry.value = arrayListOf<EntryModel>().apply {
            for (i in 1..9) {
                add(
                    EntryModel(
                        id = i.toLong(),
                        type = INPUT_TYPE_INCOME,
                        dateTime = "2023-10-0{$i`}",
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
                        type = INPUT_TYPE_PAY,
                        dateTime = "2023-10-0{$i`}",
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
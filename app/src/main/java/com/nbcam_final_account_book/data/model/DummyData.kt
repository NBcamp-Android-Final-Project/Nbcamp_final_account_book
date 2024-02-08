package com.nbcam_final_account_book.data.model

import androidx.lifecycle.MutableLiveData
import com.nbcam_final_account_book.presentation.entry.EntryModel
import com.nbcam_final_account_book.presentation.tag.TagModel
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_INCOME
import com.nbcam_final_account_book.unit.Unit.INPUT_TYPE_PAY

object DummyData {

    val liveDummyEntry: MutableLiveData<List<EntryModel>> = MutableLiveData()
    val liveDummyTag: MutableLiveData<List<TagModel>> = MutableLiveData()

    var currentTemplate: String = "template"

    var dummyBudGet: String = "2000000"

    init {
        liveDummyEntry.value = arrayListOf<EntryModel>().apply {
            for (i in 1..9) {
                add(
                    EntryModel(
                        id = i.toLong(),
                        type = INPUT_TYPE_INCOME,
                        dateTime = "2023-10-0${i}",
                        value = " ${100000 + i * 1000}",
                        tag = "월급",
                        title = "월급",
                    )
                )
            }
            add(
                EntryModel(
                    id = 11,
                    type = INPUT_TYPE_INCOME,
                    dateTime = "2023-10-10",
                    value = " ${20000}",
                    tag = "기타",
                    title = "당근",
                )

            )
            add(
                EntryModel(
                    id = 12,
                    type = INPUT_TYPE_INCOME,
                    dateTime = "2023-10-23",
                    value = " ${40000}",
                    tag = "부수익",
                    title = "배당금",
                )
            )
            add(
                EntryModel(
                    id = 13,
                    type = INPUT_TYPE_INCOME,
                    dateTime = "2023-10-7",
                    value = " ${100000}",
                    tag = "용돈",
                    title = "명절용돈",
                )
            )
            for (i in 1..9) {
                add(
                    EntryModel(
                        id = i * 20.toLong(),
                        type = INPUT_TYPE_PAY,
                        dateTime = "2023-10-0${i}",
                        value = "${1000 + i * 1000}",
                        tag = "간식비",
                        title = "포카칩",
                    )
                )
            }
            add(
                EntryModel(
                    id = 14,
                    type = INPUT_TYPE_PAY,
                    dateTime = "2023-10-20",
                    value = "${150000}",
                    tag = "쇼핑",
                    title = "겨울 옷",
                )
            )
            add(
                EntryModel(
                    id = 15,
                    type = INPUT_TYPE_PAY,
                    dateTime = "2023-10-24",
                    value = "${20000}",
                    tag = "취미",
                    title = "영화 감상",
                )
            )
            add(
                EntryModel(
                    id = 16,
                    type = INPUT_TYPE_PAY,
                    dateTime = "2023-10-25",
                    value = "${50000}",
                    tag = "의료",
                    title = "응급실",
                )
            )
        }
        liveDummyTag.value = arrayListOf<TagModel>().apply {
            for (i in 1..9) {
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
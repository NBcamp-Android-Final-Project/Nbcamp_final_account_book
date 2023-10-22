package com.nbcam_final_account_book.persentation.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nbcam_final_account_book.data.model.DummyData.liveDummyTag

class TagViewModel:ViewModel() {

    val liveDummyTagInTag : LiveData<List<TagModel>> get() = liveDummyTag
}

data class Tag(
    val icon: Int,
    val title: String
)
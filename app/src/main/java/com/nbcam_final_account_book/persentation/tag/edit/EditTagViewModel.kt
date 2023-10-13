package com.nbcam_final_account_book.persentation.tag.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nbcam_final_account_book.data.model.DummyData.liveDummyTag
import com.nbcam_final_account_book.persentation.tag.TagModel

class EditTagViewModel : ViewModel() {
    val liveDummyTagInEditTag: LiveData<List<TagModel>> get() = liveDummyTag
}

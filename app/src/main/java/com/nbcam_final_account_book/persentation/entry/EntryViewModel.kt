package com.nbcam_final_account_book.persentation.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nbcam_final_account_book.data.model.DummyData

class EntryViewModel : ViewModel() {

    val dummyLiveEntryList: LiveData<List<EntryModel>> get() = DummyData.liveDummyEntry


}
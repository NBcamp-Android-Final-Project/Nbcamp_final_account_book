package com.nbcam_final_account_book.persentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nbcam_final_account_book.data.model.DummyData.liveDummyEntry
import com.nbcam_final_account_book.persentation.entry.EntryModel

class HomeViewModel : ViewModel() {
    val liveEntryDummyDataInHome:LiveData<List<EntryModel>> get() = liveDummyEntry

}
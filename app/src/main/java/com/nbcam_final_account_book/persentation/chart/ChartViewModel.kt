package com.nbcam_final_account_book.persentation.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nbcam_final_account_book.data.model.DummyData
import com.nbcam_final_account_book.persentation.entry.EntryModel

class ChartViewModel : ViewModel() {

    val liveDummyEntryListInChart: LiveData<List<EntryModel>> get() = DummyData.liveDummyEntry



}
package com.nbcam_final_account_book.persentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel

class MainViewModel : ViewModel() {

    private val _mainLiveEntryList: MutableLiveData<List<EntryModel>> = MutableLiveData()
    val mainLiveEntryList: LiveData<List<EntryModel>> get() = _mainLiveEntryList

    private val _mainLiveTagList: MutableLiveData<List<TagModel>> = MutableLiveData()
    val mainLiveTagList: LiveData<List<TagModel>> get() = _mainLiveTagList

}
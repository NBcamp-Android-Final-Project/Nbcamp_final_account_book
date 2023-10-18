package com.nbcam_final_account_book.persentation.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.DummyData
import com.nbcam_final_account_book.data.model.remote.ResponseEntryModel
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import kotlinx.coroutines.launch

class EntryViewModel(
    private val fireRepo: FireBaseRepository
) : ViewModel() {

    val dummyLiveEntryList: LiveData<List<EntryModel>> get() = DummyData.liveDummyEntry

    private val _liveEntryList: MutableLiveData<List<ResponseEntryModel>> = MutableLiveData()
    val liveEntryList: LiveData<List<ResponseEntryModel>> get() = _liveEntryList

    fun getAllEntryListFromFirebase(template: String, type: String) {
        viewModelScope.launch {
            val resultList =
                fireRepo.getAllEntry(fireRepo.getUser(), template = template, type = type)

            _liveEntryList.value = resultList
        }
    }


}

class EntryViewModelFactory(

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
            return EntryViewModel(
                FireBaseRepositoryImpl()
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }

}
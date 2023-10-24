package com.nbcam_final_account_book.persentation.entry

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.DummyData
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import kotlinx.coroutines.launch

class EntryViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

    val dummyLiveEntryList: LiveData<List<EntryModel>> get() = DummyData.liveDummyEntry

    private val _liveValue: MutableLiveData<String?> = MutableLiveData()
    val liveValue: LiveData<String?> get() = _liveValue

    private val _liveType: MutableLiveData<String?> = MutableLiveData()
    val liveType: LiveData<String?> get() = _liveType

    //CurrentTemplateData
    private val _entryLiveCurrentTemplate: MutableLiveData<TemplateEntity?> = MutableLiveData()
    val entryLiveCurrentTemplate: LiveData<TemplateEntity?> get() = _entryLiveCurrentTemplate

    fun updateCurrentTemplateEntry(item: TemplateEntity?) {
        if (item == null) return
        _entryLiveCurrentTemplate.value = item
    }

    fun getCurrentTemplateEntry(): TemplateEntity? {
        return entryLiveCurrentTemplate.value
    }

    fun updateAmount(amount: String, type: String) {
        _liveValue.value = amount
        _liveType.value = type
        Log.d("현재 값", liveValue.value.toString())
        Log.d("현재 타입", liveType.value.toString())
    }

    fun getData(): Pair<String?, String?> {
        val currentValue = liveValue.value?.toString()
        val currentType = liveType.value?.toString()
        return Pair(currentValue, currentType)
    }

    fun insertEntity(item: EntryEntity) {
        viewModelScope.launch {
            roomRepo.insertEntry(item)
        }

    }


}

class EntryViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
            return EntryViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                )
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }

}
package com.nbcam_final_account_book.persentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

    private val _mainLiveEntryList: MutableLiveData<List<EntryModel>> = MutableLiveData()
    val mainLiveEntryList: LiveData<List<EntryModel>> get() = _mainLiveEntryList

    private val _mainLiveTagList: MutableLiveData<List<TagModel>> = MutableLiveData()
    val mainLiveTagList: LiveData<List<TagModel>> get() = _mainLiveTagList

    private val mainLiveCurrentTemplate: MutableLiveData<TemplateEntity> = MutableLiveData()


    fun insertData() {


        viewModelScope.launch {
            val id = mainLiveCurrentTemplate.value?.id ?: return@launch
            val jsonEntry = Gson().toJson(_mainLiveEntryList.value.orEmpty().toMutableList())
            val jsonTag = Gson().toJson(_mainLiveTagList.value.orEmpty().toMutableList())

            roomRepo.insertData(
                DataEntity(
                    id = id,
                    entryList = jsonEntry,
                    tagList = jsonTag,

                )
            )


        }
    }

    class MainViewModelFactory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(
                    RoomRepositoryImpl(
                        AndroidRoomDataBase.getInstance(context)
                    )
                ) as T
            } else {
                throw IllegalArgumentException("Not found ViewModel class.")
            }
        }
    }

}
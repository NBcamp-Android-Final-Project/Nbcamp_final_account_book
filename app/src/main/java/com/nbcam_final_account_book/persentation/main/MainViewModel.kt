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
import com.nbcam_final_account_book.persentation.budget.BudgetModel
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

    //EntryLiveData
    private val _mainLiveEntryList: MutableLiveData<List<EntryModel>> = MutableLiveData()
    val mainLiveEntryList: LiveData<List<EntryModel>> get() = _mainLiveEntryList

    //TagLiveData
    private val _mainLiveTagList: MutableLiveData<List<TagModel>> = MutableLiveData()
    val mainLiveTagList: LiveData<List<TagModel>> get() = _mainLiveTagList

    //BudgetLiveData
    private val _mainBudgetList: MutableLiveData<List<BudgetModel>> = MutableLiveData()
    val mainBudgetList: LiveData<List<BudgetModel>> get() = _mainBudgetList

    //CurrentTemplateData
    private val _mainLiveCurrentTemplate: MutableLiveData<TemplateEntity?> = MutableLiveData()
    val mainLiveCurrentTemplate: LiveData<TemplateEntity?> get() = _mainLiveCurrentTemplate


    fun insertData() {
        viewModelScope.launch {
            val id = mainLiveCurrentTemplate.value?.id ?: return@launch
            val jsonEntry = Gson().toJson(_mainLiveEntryList.value.orEmpty())
            val jsonTag = Gson().toJson(_mainLiveTagList.value.orEmpty())
            val jsonBudget = Gson().toJson(_mainBudgetList.value.orEmpty())

            roomRepo.insertData(
                DataEntity(
                    id = id,
                    entryList = jsonEntry,
                    tagList = jsonTag,
                    budgetList = jsonBudget
                )
            )
        }
    }

    fun updateCurrentTemplate(item: TemplateEntity?) {
        if (item == null) return
        _mainLiveCurrentTemplate.value = item
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
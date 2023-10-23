package com.nbcam_final_account_book.persentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import com.nbcam_final_account_book.persentation.budget.BudgetModel
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.tag.TagModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val roomRepo: RoomRepository,
    private val sharedProvider: SharedProvider
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

    init {
        if (loadSharedPrefCurrentUser() != null) {
            _mainLiveCurrentTemplate.value = loadSharedPrefCurrentUser()
            loadData()
        }
    }


    fun updateCurrentTemplate(item: TemplateEntity?) {
        if (item == null) return
        _mainLiveCurrentTemplate.value = item
    }

    fun addBudget(item: BudgetModel?) {
        if (item == null) return
        val currentList = mainBudgetList.value.orEmpty().toMutableList()
        currentList.add(item)
        _mainBudgetList.value = currentList
    }

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

    private fun loadData() {
        val currentTemplate = _mainLiveCurrentTemplate.value ?: return

        viewModelScope.launch {
            val loadData = roomRepo.getAllData(currentTemplate.id)
            if (loadData != null) {

                val loadEntry: List<EntryModel> =
                    Gson().fromJson(
                        loadData.entryList,
                        object : TypeToken<List<EntryModel>>() {}.type
                    )

                val loadTag: List<TagModel> =
                    Gson().fromJson(
                        loadData.tagList,
                        object : TypeToken<List<TagModel>>() {}.type
                    )

                val loadBudget: List<BudgetModel> =
                    Gson().fromJson(
                        loadData.budgetList,
                        object : TypeToken<List<BudgetModel>>() {}.type
                    )

                _mainLiveEntryList.value = loadEntry
                _mainLiveTagList.value = loadTag
                _mainBudgetList.value = loadBudget
            }
        }
    }


    //SharedPref
    fun saveSharedPrefCurrentUser(item: TemplateEntity?) {
        if (item == null) return
        val sharedPref = sharedProvider.setSharedPref("name_currentUser")
        val editor = sharedPref.edit()
        val json = Gson().toJson(item)

        editor.putString("key_currentUser", json)
        editor.apply()
    }

    private fun loadSharedPrefCurrentUser(): TemplateEntity? {
        val sharedPref = sharedProvider.setSharedPref("name_currentUser")
        val json = sharedPref.getString("key_currentUser", null)

        return Gson().fromJson(json, object : TypeToken<TemplateEntity>() {}.type)
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
                ),
                SharedProviderImpl(context)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
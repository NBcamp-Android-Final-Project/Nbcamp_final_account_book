package com.nbcam_final_account_book.persentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import com.nbcam_final_account_book.unit.Unit.liveKey
import kotlinx.coroutines.launch



class MainViewModel(
    private val roomRepo: RoomRepository,
    private val sharedProvider: SharedProvider
) : ViewModel() {

    //CurrentTemplateData
    private val _mainLiveCurrentTemplate: MutableLiveData<TemplateEntity?> = MutableLiveData()
    val mainLiveCurrentTemplate: LiveData<TemplateEntity?> get() = _mainLiveCurrentTemplate

    //EntryLiveData
    val mainLiveEntryList: LiveData<List<EntryEntity>> = liveKey.switchMap { key->
        roomRepo.getEntryByKey(key)
    }

//    val mainLiveEntryList: LiveData<List<EntryEntity>> = Transformations.switchMap(liveKey) { key ->
//        roomRepo.getEntryByKey(key)
//    }


    //TagLiveData
    private val _mainLiveTagList: MutableLiveData<List<TagEntity>> = MutableLiveData()
    val mainLiveTagList: LiveData<List<TagEntity>> get() = _mainLiveTagList

    //BudgetLiveData
    private val _mainBudgetList: MutableLiveData<List<BudgetEntity>> = MutableLiveData()
    val mainBudgetList: LiveData<List<BudgetEntity>> get() = _mainBudgetList


    init {
        if (loadSharedPrefCurrentUser() != null) {
            _mainLiveCurrentTemplate.value = loadSharedPrefCurrentUser()
//            loadData()
        }
        setKey()
    }

    fun getCurrentTemplate(): TemplateEntity? {
        return mainLiveCurrentTemplate.value
    }

    fun setKey() {
        val currentTemplate = mainLiveCurrentTemplate.value
        if (currentTemplate != null) {
            liveKey.value = currentTemplate.id
            Log.d("라이브 키값", liveKey.value.toString())
        }
    }


    fun getEntryLiveData(): LiveData<List<EntryEntity>> { //테스트를 위한 라이브 데이터 리턴
        return mainLiveEntryList
    }


    fun updateCurrentTemplate(item: TemplateEntity?) {
        if (item == null) return
        _mainLiveCurrentTemplate.value = item
    }

    fun insertData() {
        viewModelScope.launch {
            val id = mainLiveCurrentTemplate.value?.id ?: return@launch
            val jsonEntry = Gson().toJson(mainLiveEntryList.value.orEmpty())
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

    //TODO 역 직렬화해서 room database에 직접 뿌리는 형태로 바꿀 것.
    private fun loadData() {
        val currentTemplate = _mainLiveCurrentTemplate.value ?: return

        viewModelScope.launch {
            val loadData = roomRepo.getAllData(currentTemplate.id)
            if (loadData != null) {

                val loadEntry: List<EntryEntity> =
                    Gson().fromJson(
                        loadData.entryList,
                        object : TypeToken<List<EntryEntity>>() {}.type
                    )

                val loadTag: List<TagEntity> =
                    Gson().fromJson(
                        loadData.tagList,
                        object : TypeToken<List<TagEntity>>() {}.type
                    )

                val loadBudget: List<BudgetEntity> =
                    Gson().fromJson(
                        loadData.budgetList,
                        object : TypeToken<List<BudgetEntity>>() {}.type
                    )


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
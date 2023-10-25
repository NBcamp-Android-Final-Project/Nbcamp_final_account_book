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
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(
    private val roomRepo: RoomRepository,
    private val fireRepo: FireBaseRepository,
    private val sharedProvider: SharedProvider
) : ViewModel() {

    companion object {
        // mainviewModel에서만 접근해야함
        val liveKey: MutableLiveData<String> = MutableLiveData()
    }

    private val user = fireRepo.getUser()

    //CurrentTemplateData
    private val _mainLiveCurrentTemplate: MutableLiveData<TemplateEntity?> = MutableLiveData()
    val mainLiveCurrentTemplate: LiveData<TemplateEntity?> get() = _mainLiveCurrentTemplate


    //현재 데이터를 불러오는 기본 폼

    //EntryLiveData
    val mainLiveEntryList: LiveData<List<EntryEntity>> = liveKey.switchMap { key ->
        roomRepo.getLiveEntryByKey(key)
    }

    //TagLiveData
    val mainLiveTagList: LiveData<List<TagEntity>> = liveKey.switchMap { key ->
        roomRepo.getLiveTagByKey(key)
    }

    //BudgetLiveData
    val mainBudgetList: LiveData<List<BudgetEntity>> = liveKey.switchMap { key ->
        roomRepo.getLiveBudgetByKey(key)
    }

    init {
        if (loadSharedPrefCurrentUser() != null) {
            _mainLiveCurrentTemplate.value = loadSharedPrefCurrentUser()
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

    //템플릿 룸으로 백업 데이터를 저장 혹은 업데이트 해주는 로직
    //템플릿이 전환되는 순간에 호출되어야 함.
    fun updataBackupData() = with(roomRepo) {
        viewModelScope.launch(Dispatchers.IO) {
            val key = mainLiveCurrentTemplate.value?.id ?: return@launch

            val jsonEntry = Gson().toJson(getListEntryKey(key))
            val jsonTag = Gson().toJson(getListTagKey(key))
            val jsonBudget = Gson().toJson(getListBudgetByKey(key))

            val data = DataEntity(
                id = key,
                entryList = jsonEntry,
                tagList = jsonTag,
                budgetList = jsonBudget
            )

            if (getDataByKey(key) == null) {
                insertData(data)
            } else {
                updateData(data)
            }


        }
    }

    // todo fireabse에서 가져온 데이터로 load하게 변환하는 로직도 추가되어야 함
    // 현재 형태는 템플릿지 전환될 때 마다 데이터를 가져오는 형태임
    // 이 형태가 굳이 필요 있나 고민해보면 없음
    private fun loadData() = with(roomRepo) {
        val currentTemplate = _mainLiveCurrentTemplate.value


        if (currentTemplate != null) {
            viewModelScope.launch {
                val loadData = getDataByKey(currentTemplate.id)
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
                    insertEntryList(loadEntry)
                    insertBudgetList(loadBudget)
                    insertTagList(loadTag)
                }
            }
        }

    }

    //반드시 로그아웃 시 호출되어야 함.
    fun backupDataByLogOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val dataList: List<DataEntity> = roomRepo.getAllData()
            for (dataEntity in dataList) {
                fireRepo.updateData(user, dataEntity)
            }
            roomRepo.deleteAllData()
        }
    }

    //firebase 데이터 동기화
    fun synchronizationData() {

        viewModelScope.launch {
            val backUpTemplate = fireRepo.getAllTemplate(user)
            val backUpData = fireRepo.getBackupData(user)

            roomRepo.insertDataList(backUpData)
            with(roomRepo) {
                insertTemplateList(backUpTemplate)
                insertDataList(backUpData)

                val currentTemplate = _mainLiveCurrentTemplate.value

                //로그인 시 제일 첫 번쨰 템플릿이 디폴트로 들어옴
                _mainLiveCurrentTemplate.value = backUpTemplate[0]

                if (currentTemplate != null) {
                    viewModelScope.launch {

                        if (backUpData != null) {
                            for (loadData in backUpData) {
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
                                insertEntryList(loadEntry)
                                insertBudgetList(loadBudget)
                                insertTagList(loadTag)
                            } // for(loadData in backUpData)

                        } //backUpData != null
                    }
                } //  if (currentTemplate != null

            } //   with(roomRepo)

            saveSharedPrefIsLogin(true)
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

    fun saveSharedPrefIsLogin(isLogin: Boolean) {
        val sharedPref = sharedProvider.setSharedPref("name_isLogin")
        val editor = sharedPref.edit()

        editor.putBoolean("key_isLogin", isLogin)
        editor.apply()

    }

    fun loadSharedPrefIsLogin(): Boolean {
        val sharedPref = sharedProvider.setSharedPref("name_isLogin")

        return sharedPref.getBoolean("key_isLogin", false)
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
                FireBaseRepositoryImpl(),
                SharedProviderImpl(context),

                ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
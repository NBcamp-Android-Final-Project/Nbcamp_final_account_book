package com.nbcam_final_account_book.persentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

    val mainUserDataLive: LiveData<List<UserDataEntity>> = roomRepo.getAllUserDataLiveData()

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

    //MyPage Pin LiveData
    private val _pin = MutableLiveData<String>()
    val pin: LiveData<String> get() = _pin

    private val _isPinSet = MutableLiveData<Boolean>()
    val isPinSet: LiveData<Boolean> get() = _isPinSet

    var isSwitch = false // 클릭한 버튼 정보를 저장하는 변수

    init {
        if (loadSharedPrefCurrentUser() != null) {
            _mainLiveCurrentTemplate.value = loadSharedPrefCurrentUser()
        }
        setKey()

        _pin.value = loadSharedPrefPinNumber()
        _isPinSet.value = _pin.value?.isNotEmpty() ?: false
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

    suspend fun updateCurrentTemplateInCo(item: TemplateEntity?) {
        if (item == null) return
        _mainLiveCurrentTemplate.postValue(item)

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

    //반드시 로그아웃 시 호출되어야 함.


    suspend fun backupDataByLogOut(): Boolean = withContext(Dispatchers.IO) {

        val templateList = roomRepo.getAllListTemplate()
        val dataList: List<DataEntity> = roomRepo.getAllData()
        val deleteKey = roomRepo.getAllDelete()

        val templateListDeferred = async { fireRepo.setTemplateList(user, templateList) }
        val dataListDeferred = async { fireRepo.updateDataList(user, dataList) }

        templateListDeferred.await()
        dataListDeferred.await()

        if (deleteKey.isNotEmpty()) {
            for (key in deleteKey) {
                fireRepo.deleteData(user, key.key)
                fireRepo.deleteTemplate(user, key.key)
            }
        }
        roomRepo.deleteAllDeleteEntity()
        Log.d("딜리트", deleteKey.size.toString())
        saveSharedPrefIsLogin(false)
        roomRepo.deleteAllData()

        true

    }

    fun backupData() {
        viewModelScope.launch(Dispatchers.IO) {
            val dataList: List<DataEntity> = roomRepo.getAllData()
            val templateList: List<TemplateEntity> = roomRepo.getAllListTemplate()
            val deleteKey = roomRepo.getAllDelete()
            fireRepo.setTemplateList(user, templateList)
            fireRepo.updateDataList(user, dataList)


            if (deleteKey.isNotEmpty()) {
                for (key in deleteKey) {
                    fireRepo.deleteData(user, key.key)
                    fireRepo.deleteTemplate(user, key.key)
                }
            }

            roomRepo.deleteAllDeleteEntity()
            Log.d("딜리트", deleteKey.size.toString())
        }
    }

    //firebase 데이터 동기화
    //Todo 동기화 실패 시 예외처리하기
    fun firstStartSynchronizationData() {

        viewModelScope.launch {
            val backUpTemplate = fireRepo.getAllTemplate(user)
            val backUpData = fireRepo.getBackupData(user)
            if (backUpTemplate.isEmpty() || backUpData.isEmpty()) return@launch

            roomRepo.insertDataList(backUpData)
            with(roomRepo) {
                insertTemplateList(backUpTemplate)
                insertDataList(backUpData)

                val currentTemplate = _mainLiveCurrentTemplate.value

                //로그인 시 제일 첫 번쨰 템플릿이 디폴트로 들어옴
                _mainLiveCurrentTemplate.value = backUpTemplate[0]

                if (currentTemplate != null) {
                    viewModelScope.launch {

                        if (backUpData.isNotEmpty()) {
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

    //todo 다중 템플릿 구조로 변경 시 현재 템플릿 = key값으로 찾는 구조로 만들 것
    fun synchronizationDataWithBtn() {

        viewModelScope.launch {

            val backUpTemplate = fireRepo.getAllTemplate(user)
            val backUpData = fireRepo.getBackupData(user)

            if (backUpData.isEmpty()) return@launch

            roomRepo.insertDataList(backUpData)
            with(roomRepo) {
                deleteAllTemplate()
                deleteAllBudget()
                deleteAllEntry()
                deleteAllData()
                deleteAllDeleteEntity()
                insertTemplateList(backUpTemplate)
                insertDataList(backUpData)

                val currentTemplate = _mainLiveCurrentTemplate.value

                //로그인 시 제일 첫 번쨰 템플릿이 디폴트로 들어옴
                _mainLiveCurrentTemplate.value = backUpTemplate[0]

                if (currentTemplate != null) {
                    viewModelScope.launch {

                        if (backUpData.isNotEmpty()) {
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

    // 앱 자체 비밀번호 설정을 위한 SharedPreferences
    fun saveSharedPrefPinNumber(num: String) {
        _pin.value = num
        _isPinSet.value = num.isNotEmpty()

        val sharedPref = sharedProvider.setSharedPref("pin_number")
        val editor = sharedPref.edit()
        editor.putString("key_pin", num)
        editor.apply()
    }

    private fun loadSharedPrefPinNumber(): String {
        val sharedPref = sharedProvider.setSharedPref("pin_number")
        return sharedPref.getString("key_pin", "") ?: ""
    }

    fun removeSharedPrefPinNumber() {
        _pin.value = ""
        _isPinSet.value = false

        val sharedPref = sharedProvider.setSharedPref("pin_number")
        val editor = sharedPref.edit()
        editor.remove("key_pin")
        editor.apply()
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
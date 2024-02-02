package com.nbcam_final_account_book.presentation.template

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl

class TemplateViewModel(
    private val roomRepo: RoomRepository,
    private val sharedProvider: SharedProvider,
    private val fireRepo: FireBaseRepository
) : ViewModel() {

    private val _liveTitle: MutableLiveData<String?> = MutableLiveData()
    val liveTitle: LiveData<String?> get() = _liveTitle

    private val _liveType: MutableLiveData<TemplateType?> = MutableLiveData()
    val liveType: LiveData<TemplateType?> get() = _liveType

    init {
        _liveType.postValue(loadType())
    }

    fun updateLiveTitle(title: String?) {
        if (title != null) {
            _liveTitle.value = title
        }
    }

    fun updateType(type: TemplateType?) {
        if (type == null) return
        _liveType.postValue(type)
    }

    fun type(): TemplateType? {
        return liveType.value
    }


    fun getCurrentTitle(): String {
        return liveTitle.value.toString()
    }

    fun saveIsFirst(isFirst: Boolean) {
        val sharedPref = sharedProvider.setSharedPref("name_isFirst")
        val editor = sharedPref.edit()

        editor.putBoolean("key_isFirst", isFirst)
        editor.apply()
    }

    fun saveType(type: TemplateType?) {
        Log.d("세이브타입", type.toString())
        val sharedPref = sharedProvider.setSharedPref("name_template_type")
        val editor = sharedPref.edit()

        editor.putString("key_template_type", type?.name)
        editor.apply()
    }

    private fun loadType(): TemplateType? {
        val sharedPref = sharedProvider.setSharedPref("name_template_type")
        val type = sharedPref.getString("key_template_type", null)

        Log.d("로드타입", type.toString())

        return TemplateType.templateType(type)
    }

    fun logout() { // firebase 로그아웃
        fireRepo.logout()
    }


}

class TemplateViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateViewModel::class.java)) {
            return TemplateViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                ),
                SharedProviderImpl(context),
                FireBaseRepositoryImpl()
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
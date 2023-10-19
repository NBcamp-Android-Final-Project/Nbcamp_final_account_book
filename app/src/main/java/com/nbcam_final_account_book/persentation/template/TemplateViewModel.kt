package com.nbcam_final_account_book.persentation.template

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import com.nbcam_final_account_book.persentation.login.LoginViewModel
import kotlinx.coroutines.launch

class TemplateViewModel(
    private val roomRepo: RoomRepository,
    private val sharedProvider: SharedProvider,
    private val fireRepo: FireBaseRepository
) : ViewModel() {

    fun saveIsFirst(isFirst: Boolean) {
        val sharedPref = sharedProvider.setSharedPref("name_isFirst")
        val editor = sharedPref.edit()

        editor.putBoolean("key_isFirst", isFirst)
        editor.apply()
    }

    fun insertFirstTemplate(title: String) {
        viewModelScope.launch {
            roomRepo.insertFirstTemplate(title)
        }
    }

    fun addTemplateFirst() {
        viewModelScope.launch {
            fireRepo.setTemplate(fireRepo.getUser(), roomRepo.selectFirstTemplate())
        }
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
package com.nbcam_final_account_book.persentation.firstpage.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class LoginViewModel(
    private val fireRepo: FireBaseRepository,
    private val roomRepo: RoomRepository
) : ViewModel() {

    fun updateUser(user: UserDataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = viewModelScope.async(Dispatchers.IO) {
                roomRepo.insertUserData(user)
            }
            result.await()
            fireRepo.updateUserInFireStore(user)
        }
    }


}

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                FireBaseRepositoryImpl(),
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                )
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
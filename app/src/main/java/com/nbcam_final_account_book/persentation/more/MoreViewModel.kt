package com.nbcam_final_account_book.persentation.more

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import kotlinx.coroutines.launch

class MoreViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

     fun cleanRoom()= with(roomRepo) {
        viewModelScope.launch {
            deleteAllTemplate()
            deleteAllBudget()
            deleteAllEntry()
        }
    }

}

class MoreViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoreViewModel::class.java)) {
            return MoreViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                )
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
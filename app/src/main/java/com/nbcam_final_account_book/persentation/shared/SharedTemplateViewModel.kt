package com.nbcam_final_account_book.persentation.shared

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import kotlinx.coroutines.launch


class SharedTemplateViewModel(
    private val fireRepo: FireBaseRepository
) : ViewModel() {



}

class SharedTemplateViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedTemplateViewModel::class.java)) {
            return SharedTemplateViewModel(
                FireBaseRepositoryImpl()
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
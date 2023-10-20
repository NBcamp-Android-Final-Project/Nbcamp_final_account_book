package com.nbcam_final_account_book.persentation.lock.locksetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class LockSettingViewModel(private val shardProvider: SharedProvider) : ViewModel() {
    // TODO: Implement the ViewModel
}

class LockSettingViewModelFactory(private val sharedProvider: SharedProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LockSettingViewModel::class.java)) {
            return LockSettingViewModel(sharedProvider) as T
        }
        throw IllegalArgumentException("Not found ViewModel class.")
    }
}
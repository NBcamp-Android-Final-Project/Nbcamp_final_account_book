package com.nbcam_final_account_book.persentation.lock.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class PinViewModel(sharedProvider: SharedProvider): ViewModel() {

    fun arePinMatching(pin1: String, pin2: String): Boolean {
        return pin1 == pin2
    }
}

class PinViewModelFactory(private val sharedProvider: SharedProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PinViewModel::class.java)) {
            return PinViewModel(sharedProvider) as T
        }
        throw IllegalArgumentException("Not found ViewModel class.")
    }
}
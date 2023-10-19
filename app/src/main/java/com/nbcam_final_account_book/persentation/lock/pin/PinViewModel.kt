package com.nbcam_final_account_book.persentation.lock.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class PinViewModel(sharedProvider: SharedProvider): ViewModel() {

    companion object {
        const val APP_LOCK_PASSWORD = "AppLockPassword"
        const val LOCK_PASSWORD = "lockPassword"
    }

    private val sharedPreferences = sharedProvider.setSharedPref(APP_LOCK_PASSWORD)

    fun arePinMatching(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    fun savePin(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LOCK_PASSWORD, password)
        editor.apply()
    }

    fun getSavedPassword(): String? {
        return sharedPreferences.getString(LOCK_PASSWORD, null)
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
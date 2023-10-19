package com.nbcam_final_account_book.persentation.lock.locksetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class LockSettingViewModel(sharedProvider: SharedProvider): ViewModel() {

    companion object {
        const val APP_LOCK_PASSWORD = "AppLockPassword"
        const val LOCK_PASSWORD = "lockPassword"
    }

    private val sharedPreferences = sharedProvider.setSharedPrefUserToken(APP_LOCK_PASSWORD)

    fun arePasswordMatching(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LOCK_PASSWORD, password)
        editor.apply()
    }

    fun getSavedPassword(): String? {
        return sharedPreferences.getString(LOCK_PASSWORD, null)
    }
}

class LockSettingViewModelFactory(private val sharedProvider: SharedProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LockSettingViewModel::class.java)) {
            return LockSettingViewModel(sharedProvider) as T
        }
        throw IllegalArgumentException("Not found ViewModel class.")
    }
}
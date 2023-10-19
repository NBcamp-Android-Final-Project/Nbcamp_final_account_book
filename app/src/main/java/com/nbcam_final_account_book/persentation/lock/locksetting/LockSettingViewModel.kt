package com.nbcam_final_account_book.persentation.lock.locksetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class LockSettingViewModel(sharedProvider: SharedProvider): ViewModel() {
    private val sharedPreferences = sharedProvider.setSharedPref("AppLockPassword")

    private var firstInput: String? = null

    fun validateAndSavePassword(password: String): Boolean{
        val savedPassword = sharedPreferences.getString("lockPassword", "")
//        return password == savedPassword
        if (firstInput == null) {
            firstInput = password
            return false
        } else if (password == savedPassword && password == firstInput) {
            savePassword(password)
            return true
        }
        return false
    }

    private fun savePassword(password:String) {
        val editor = sharedPreferences.edit()
        editor.putString("lockPassword", password)
        editor.apply()
    }

    fun isFirstInput(): Boolean {
        return firstInput == null
    }

    fun setFirstInputPassword(password: String) {
        firstInput = password
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
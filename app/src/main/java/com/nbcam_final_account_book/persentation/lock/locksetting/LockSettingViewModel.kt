package com.nbcam_final_account_book.persentation.lock.locksetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class LockSettingViewModel(sharedProvider: SharedProvider) : ViewModel() {

    companion object {
        const val LOCK_SETTING_PREF = "LockSettingPref"
        const val LOCK_TYPE = "LockType"
        const val NONE = "선택안함"
        const val PIN = "비밀번호"
    }

    private val sharedPreferences = sharedProvider.setSharedPref(LOCK_SETTING_PREF)

    private val _selectedLockType = MutableLiveData<String>()
    val selectedLockType: LiveData<String> get() = _selectedLockType

    init {
        _selectedLockType.value = getLockType()
    }

    fun setLockType(lockType: String) {
        saveLockType(lockType)
        _selectedLockType.value = lockType
    }

    private fun saveLockType(lockType: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LOCK_TYPE, lockType)
        editor.apply()
    }

    private fun getLockType(): String {
        return sharedPreferences.getString(LOCK_TYPE, NONE) ?: NONE
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
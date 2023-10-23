package com.nbcam_final_account_book.persentation.lock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class LockSharedViewModel(private val sharedProvider: SharedProvider) : ViewModel() {
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _isPinSet = MutableLiveData<Boolean>()
    val isPinSet: LiveData<Boolean> get() = _isPinSet

    init {
        _password.value = loadPassword()
        _isPinSet.value = _password.value!!.isNotEmpty()
    }

    fun savePassword(pin: String) {
        _password.value = pin
        val isPasswordSet = pin.isNotEmpty()
        _isPinSet.value = isPasswordSet

        Log.d("LockSharedViewModel", "이때 호출 된다!!!!")
        Log.d("LockSharedViewModel", "_password: ${_password.value}")
        Log.d("LockSharedViewModel", "_isPinSet: ${_isPinSet.value}")
    }

    fun clearPassword() {
        _password.value = ""
        _isPinSet.value = false
    }

    fun setIsPinSet(isPinSet: Boolean) {
        _isPinSet.value = isPinSet
    }

    fun getIsPinSet(): Boolean {
        return _isPinSet.value ?: false
    }

    fun loadPassword(): String {
        return _password.value ?: ""
    }
}

class LockViewModelFactory(private val sharedProvider: SharedProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LockSharedViewModel::class.java)) {
            return LockSharedViewModel(sharedProvider) as T
        }
        throw IllegalArgumentException("Not found ViewModel class.")
    }
}
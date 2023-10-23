package com.nbcam_final_account_book.persentation.lock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LockSharedViewModel() : ViewModel() {
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _isPinSet = MutableLiveData<Boolean>()
    val isPinSet: LiveData<Boolean> get() = _isPinSet

    init {
        _password.value = loadPassword()
        _isPinSet.value = _password.value?.isNotEmpty() ?: false
    }

    fun savePassword(pin: String) {
        _password.value = pin
        _isPinSet.value = pin.isNotEmpty()

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

    private fun loadPassword(): String {
        //TODO: sharedPreferences에서 저장된 비밀번호 로드해오기
        return _password.value ?: ""
    }
}
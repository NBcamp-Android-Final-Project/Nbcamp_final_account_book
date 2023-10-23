package com.nbcam_final_account_book.persentation.lock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LockSharedViewModel : ViewModel() {
    private val _pin = MutableLiveData<String>()
    val pin: LiveData<String> get() = _pin

    private val _isPinSet = MutableLiveData<Boolean>()
    val isPinSet: LiveData<Boolean> get() = _isPinSet

    init {
        _pin.value = loadPassword()
        _isPinSet.value = _pin.value?.isNotEmpty() ?: false
    }

    fun savePin(pin: String) {
        _pin.value = pin
        _isPinSet.value = pin.isNotEmpty()

        Log.d("LockSharedViewModel", "이때 호출 된다!!!!")
        Log.d("LockSharedViewModel", "_pin: ${_pin.value}")
        Log.d("LockSharedViewModel", "_isPinSet: ${_isPinSet.value}")
    }

    fun clearPin() {
        _pin.value = ""
        _isPinSet.value = false
    }


    private fun loadPassword(): String {
        //TODO: sharedPreferences에서 저장된 비밀번호 로드해오기
        return _pin.value ?: ""
    }
}
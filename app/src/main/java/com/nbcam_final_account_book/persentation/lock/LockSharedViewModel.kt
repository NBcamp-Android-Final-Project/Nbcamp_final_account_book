package com.nbcam_final_account_book.persentation.lock

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LockSharedViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val MY_Prefs = "MyPrefs"
        const val PIN_KEY = "pin_key"
    }

    private val sharedPrefs: SharedPreferences = application.getSharedPreferences(MY_Prefs, Application.MODE_PRIVATE)

    private val _pin = MutableLiveData<String>()
    val pin: LiveData<String> get() = _pin

    private val _isPinSet = MutableLiveData<Boolean>()
    val isPinSet: LiveData<Boolean> get() = _isPinSet

    init {
        _pin.value = loadPassword()
        _isPinSet.value = _pin.value?.isNotEmpty() ?: false
    }

    fun savePin(num: String) {
        _pin.value = num
        _isPinSet.value = num.isNotEmpty()

        val editor = sharedPrefs.edit()
        editor.putString(PIN_KEY, num)
        editor.apply()
    }

    fun clearPin() {
        _pin.value = ""
        _isPinSet.value = false

        val editor = sharedPrefs.edit()
        editor.putString(PIN_KEY, "")
        editor.apply()
    }


    private fun loadPassword(): String {
        return sharedPrefs.getString(PIN_KEY, "") ?: ""
    }
}
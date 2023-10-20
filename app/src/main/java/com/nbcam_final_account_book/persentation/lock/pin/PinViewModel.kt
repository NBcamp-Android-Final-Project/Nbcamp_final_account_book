package com.nbcam_final_account_book.persentation.lock.pin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider

class PinViewModel(sharedProvider: SharedProvider): ViewModel() {

    companion object {
        const val PIN_VIEW_MODEL = "PinViewModel"
        const val APP_LOCK_PIN = "AppLockPin"
        const val LOCK_PIN = "lockPin"
    }

    private val sharedPreferences = sharedProvider.setSharedPref(APP_LOCK_PIN)

    fun arePinMatching(pin1: String, pin2: String): Boolean {
        return pin1 == pin2
    }

    fun savePin(pin: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LOCK_PIN, pin)
        editor.apply()

        Log.d(PIN_VIEW_MODEL, "저장된 비밀번호: $pin")
    }

    fun getSavedPin(): String? {
        return sharedPreferences.getString(LOCK_PIN, null)
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
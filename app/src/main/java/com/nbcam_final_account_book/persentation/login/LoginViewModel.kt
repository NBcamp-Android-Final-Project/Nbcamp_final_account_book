package com.nbcam_final_account_book.persentation.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl

class LoginViewModel(
    private val sharedProvider: SharedProvider
) : ViewModel() {

    private val _liveToken: MutableLiveData<String> = MutableLiveData()
    val liveToken: LiveData<String> get() = _liveToken

    fun saveUserToken(token: String){

    }

}

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                SharedProviderImpl(context)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
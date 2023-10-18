package com.nbcam_final_account_book.persentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.persentation.entry.EntryViewModel
import com.nbcam_final_account_book.unit.sharedprovider.SharedProvider
import com.nbcam_final_account_book.unit.sharedprovider.SharedProviderImpl

class LoginViewModel(
    private val sharedProvider: SharedProvider
) : ViewModel() {



}

class LoginViewModelFactory(
    private val context: Context
):ViewModelProvider.Factory{
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
package com.nbcam_final_account_book.persentation.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class LoginViewModel(
) : ViewModel() {

}







class LoginViewModelFactory(

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(

            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
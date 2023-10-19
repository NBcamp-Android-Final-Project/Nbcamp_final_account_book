package com.nbcam_final_account_book.persentation.login


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl


class LoginViewModel(
    private val sharedProvider: SharedProvider
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun loadSharedisFirst(): Boolean {
        val sharedPref = sharedProvider.setSharedPref("name_isFirst")

        return sharedPref.getBoolean("key_isFirst", false)
    }

    fun logInWithEmailAndPassword(email: String?, password: String?): Boolean {

        var isSuccess: Boolean = false

        return if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    isSuccess = task.isSuccessful

                }
            isSuccess
        } else {
            false
        }

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
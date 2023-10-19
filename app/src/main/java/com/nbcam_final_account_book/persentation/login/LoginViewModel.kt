package com.nbcam_final_account_book.persentation.login


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel(
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

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
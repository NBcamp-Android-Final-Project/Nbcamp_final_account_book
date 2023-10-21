package com.nbcam_final_account_book.persentation.firstpage


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LoginViewModel(
    private val firebaseRepo: FireBaseRepository,
    private val sharedProvider: SharedProvider
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun loadSharedisFirst(): Boolean {
        val sharedPref = sharedProvider.setSharedPref("name_isFirst")

        return sharedPref.getBoolean("key_isFirst", false)
    }

    suspend fun getAllTemplateSize(): Int = withContext(Dispatchers.IO) {

        val list = firebaseRepo.getAllTemplate(firebaseRepo.getUser())

        list.size

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
                FireBaseRepositoryImpl(),
                SharedProviderImpl(context)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
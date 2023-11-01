package com.nbcam_final_account_book.persentation.firstpage.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpViewModel() : ViewModel() {

    private lateinit var auth: FirebaseAuth

    suspend fun signUp(email: String, password: String, name: String): Boolean =
        withContext(Dispatchers.IO) {
           try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()

                val user = auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                user?.updateProfile(profileUpdates)?.await()
                true
            } catch (e: Exception) {

                false
            }
        }

}
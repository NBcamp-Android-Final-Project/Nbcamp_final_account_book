package com.nbcam_final_account_book.persentation.mypage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

    companion object {
        const val MY_PAGE_VIEW_MODEL = "MyPageViewModel"
    }

    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> get() = _name

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    private val _photoUrl = MutableLiveData<Uri?>()
    val photoUrl: LiveData<Uri?> get() = _photoUrl

    private val user = Firebase.auth.currentUser

    fun cleanRoom() = with(roomRepo) {
        viewModelScope.launch {
            deleteAllTemplate()
            deleteAllBudget()
            deleteAllEntry()
        }
    }

    fun getName() {
        val name = user?.displayName
        _name.value = name
    }

    fun getEmail() {
        val email = user?.email
        _email.value = email
    }

    fun getPhotoUrl() {
        val photoUrl = user?.photoUrl
        _photoUrl.value = photoUrl
    }

    fun updateUserName(newName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(MY_PAGE_VIEW_MODEL, "User name updated.")
            } else {
                Log.w(MY_PAGE_VIEW_MODEL, "User name update failed.", task.exception)
            }
        }
    }

    fun updateUserPhotoUrl(newPhotoUrl: Uri) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(newPhotoUrl)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(MY_PAGE_VIEW_MODEL, "User profile picture updated.")
                } else {
                    Log.w(MY_PAGE_VIEW_MODEL, "User profile picture update failed.", task.exception)

                }
            }
    }
}

class MyPageViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            return MyPageViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                )
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
package com.nbcam_final_account_book.persentation.mypage

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val fireRepo: FireBaseRepository,
    private val roomRepo: RoomRepository,
    sharedProvider: SharedProvider
) : ViewModel() {

    companion object {
        const val MY_PAGE_VIEW_MODEL = "MyPageViewModel"
        const val MY_PAGE_PREFS = "MyPagePrefS"
        const val BACKUP_TIME = "BackupTime"
        const val SYNC_TIME = "SyncTime"
    }

    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> get() = _name

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    private val _photoUrl = MutableLiveData<Uri?>()
    val photoUrl: LiveData<Uri?> get() = _photoUrl

    private val user = Firebase.auth.currentUser
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private val sharedPrefs: SharedPreferences = sharedProvider.setSharedPref(MY_PAGE_PREFS)

    fun saveBackupTime(time: String) {
        sharedPrefs.edit().putString(BACKUP_TIME, time).apply()
    }

    fun getBackupTime(): String? {
        return sharedPrefs.getString(BACKUP_TIME, "")
    }

    fun saveSyncTime(time: String) {
        sharedPrefs.edit().putString(SYNC_TIME, time).apply()
    }

    fun getSyncTime(): String? {
        return sharedPrefs.getString(SYNC_TIME, "")
    }

    fun cleanRoom() = with(roomRepo) {
        viewModelScope.launch {
            deleteAllTemplate()
            deleteAllBudget()
            deleteAllEntry()
            deleteAllTag()
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

    fun updateUserName(newName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(MY_PAGE_VIEW_MODEL, "User name updated.")
                updateUserNameInFirestore(newName)
            } else {
                Log.w(MY_PAGE_VIEW_MODEL, "User name update failed.", task.exception)
            }
        }
    }

    private fun updateUserNameInFirestore(newName: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (email != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Users").document(email)
            val updates = hashMapOf<String, Any>("name" to newName)

            userRef.update(updates)
                .addOnSuccessListener {
                    Log.d("Firestore", "User name updated successfully in Firestore.")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error updating user name in Firestore: $e")
                }
        } else {
            Log.e("Firestore", "User email is null.")
        }
    }

    fun downloadProfileImage(onSuccess: (Uri) -> Unit, onFailure: (Exception) -> Unit) {
        val userUid = user?.uid
        viewModelScope.launch {
            val userDao = userUid?.let { roomRepo.getUserDataByKey(it) }
            if (userDao != null) {
                val photoUrl = userDao.img
                if (photoUrl.isNotEmpty()) {
                    _photoUrl.value = Uri.parse(photoUrl)
                    onSuccess(Uri.parse(photoUrl))
                } else {
                    onFailure(Exception("Image URL is empty"))
                }
            } else {
                onFailure(Exception("User not found in Room database"))
            }
        }
    }

    fun uploadProfileImage(imageUri: Uri) {
        Log.d(MY_PAGE_VIEW_MODEL, "Uploading profile image: Uri=$imageUri")
        val user = FirebaseAuth.getInstance().currentUser
        val userUid = user?.uid
        val imageRef = storageRef.child("profile_images/$userUid.jpg")

        // Storage에 이미지를 업로드
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // 업로드 성공 시, 업로드된 이미지의 다운로드 URL을 가져옴
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // 다운로드 URL로 Firestore와 Room 업데이트
                    updateUserData(user?.uid, uri)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseStorage", "Image upload failed: $exception")
            }
    }

    // Firestore, Room에 이미지 업데이트
    private fun updateUserData(uid: String?, photoUrl: Uri) {
        Log.d(MY_PAGE_VIEW_MODEL, "Updating user data: UID=$uid, Photo URL=$photoUrl")
        if (uid != null) {
            // Firestore 업데이트
            val db = FirebaseFirestore.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            val email = user?.email
            Log.d(MY_PAGE_VIEW_MODEL, "Firestore update: User email=$email")

            val userRef = email?.let { db.collection("Users").document(it) }
            val updates = hashMapOf<String, Any>("photoUrl" to photoUrl)

            userRef?.update(updates)?.addOnSuccessListener {
                Log.d("Firestore", "Photo URL updated successfully.")
            }?.addOnFailureListener { e ->
                Log.e("Firestore", "Error updating photo URL: $e")
            }

            // Room 업데이트
            viewModelScope.launch {
                val userDao = roomRepo.getUserDataByKey(uid)
                if (userDao != null) {
                    userDao.img = photoUrl.toString()
                    updateUser(userDao)
                }
            }

            // LiveData 업데이트
            _photoUrl.value = photoUrl
        } else {
            Log.e("Firestore", "User email is null.")
        }
    }

    fun deleteProfileImageFromStorage(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val userUid = user?.uid
        val imageRef = storageRef.child("profile_images/$userUid.jpg")

        // Storage에서 이미지 삭제
        imageRef.delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun deleteProfileImageFromFirestoreAndRoom() {
        val user = Firebase.auth.currentUser
        val email = user?.email
        val uid = user?.uid

        // Firestore에서 이미지 URL 삭제
        if (email != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Users").document(email)
            val updates = hashMapOf<String, Any>("photoUrl" to "")

            userRef.update(updates)
                .addOnSuccessListener {
                    Log.d("Firestore", "이미지 URL 성공적으로 삭제됨.")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "이미지 URL 삭제 오류: $e")
                }
        }

        // Room에서 이미지 URL 삭제
        viewModelScope.launch {
            val userDao = uid?.let { roomRepo.getUserDataByKey(it) }
            if (userDao != null) {
                userDao.img = ""
                updateUser(userDao)
            }
        }

        // LiveData 업데이트
        _photoUrl.value = null
    }


    private fun deleteData(user: String, key: String) {
        viewModelScope.launch {
            fireRepo.deleteData(user, key)
        }
    }

    private fun deleteUserInFireStore(email: String) {
        viewModelScope.launch {
            fireRepo.deleteUserInFireStore(email)
        }
    }

    fun deleteAllData(user: String, key: String, email: String) {
        deleteData(user, key)
        deleteUserInFireStore(email)
    }

    /**
     * Room 으로 기기에 현재 로그인 User 정보 저장하는 영역
     */
    private fun currentUser(key: String, name: String, id: String, img: String): UserDataEntity {
        return UserDataEntity(key = key, name = name, id = id, img = img)
    }

    private fun updateUser(user: UserDataEntity) {
        viewModelScope.launch {
            roomRepo.updateUser(user)
        }
    }

    fun storeUser(key: String, name: String, id: String, img: String) {
        val user = currentUser(key, name, id, img)
        updateUser(user)
    }
}

class MyPageViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            return MyPageViewModel(
                FireBaseRepositoryImpl(),
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                ),
                SharedProviderImpl(context)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
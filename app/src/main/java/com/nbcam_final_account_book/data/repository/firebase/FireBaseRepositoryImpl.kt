package com.nbcam_final_account_book.data.repository.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.unit.Unit.TEMPLATE_DATA
import com.nbcam_final_account_book.unit.Unit.TEMPLATE_LIST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class FireBaseRepositoryImpl(

) : FireBaseRepository {

	//user 관리
	override fun getUser(): String {
		val user = FirebaseAuth.getInstance().currentUser
		return user?.uid ?: ""
	}

	override fun logout() {
		val auth = FirebaseAuth.getInstance()
		auth.signOut()
	}

	override suspend fun updateUserInFireStore(user: UserDataEntity) {
		val db = Firebase.firestore
		val id = user.id

		val userData = hashMapOf(
			"uid" to user.key,
			"name" to user.name,
			"email" to user.id,
			"photoUrl" to user.img,
		)


		db.collection("Users").document(user.id).set(userData)
			.addOnSuccessListener {
				Log.d(
					"firestore successfully",
					"DocumentSnapshot successfully written!"
				)
			}
			.addOnFailureListener { e -> Log.w("firestore error", "Error writing document", e) }


	}

	override suspend fun deleteUserInFireStore(email: String) {
		val db = Firebase.firestore
		db.collection("Users").document(email).delete()
	}

	override suspend fun getUserInFireStore(uid: String): UserDataEntity? {
		val db = Firebase.firestore

		try {
			val responseData = db.collection("Users")
				.whereEqualTo("uid", uid)
				.get()
				.await()

			var resultUserData: UserDataEntity? = null

			for (document in responseData) {
				val uid = document.getString("uid") ?: ""
				val name = document.getString("name") ?: ""
				val email = document.getString("email") ?: ""
				val photoUrl = document.getString("photoUrl") ?: ""

				val userData = UserDataEntity(key = uid, name = name, id = email, img = photoUrl)
				resultUserData = userData
			}

			return resultUserData

		} catch (e: Exception) {
			Log.e("FirebaseRepo", "UserData 가져오기 중 오류 발생")
			return null
		}

	}

    override suspend fun getAllUsers(): List<UserDataEntity> = withContext(Dispatchers.IO) {
        val db = Firebase.firestore

        try {
            val querySnapshot = db.collection("Users").get().await()
            val users = querySnapshot.documents.mapNotNull { document ->
                document.toObject(UserDataEntity::class.java)
            }
            // 성공적으로 데이터를 가져온 경우 로그 출력
            Log.d("FirebaseRepo", "가져온 UserData의 개수: ${users.size}")
            return@withContext users

        } catch (e: Exception) {
            // 오류 발생 시 로그 출력
            Log.e("FirebaseRepo", "모든 UserData 가져오기 중 오류 발생: ${e.message}")
            emptyList()
        }
    }

    override suspend fun searchUserDataInFireStore(keyword: String): List<UserDataEntity> =
        withContext(Dispatchers.IO) {
        val db = Firebase.firestore

		try {
			val nameQuery = db.collection("Users")
				.whereEqualTo("name", keyword)
				.get()
				.await()

			val emailQuery = db.collection("Users")
				.whereEqualTo("email", keyword)
				.get()
				.await()

			val resultUserDataList: MutableList<UserDataEntity> = mutableListOf()

			for (document in nameQuery) {
				val uid = document.getString("uid") ?: ""
				val name = document.getString("name") ?: ""
				val email = document.getString("email") ?: ""
				val photoUrl = document.getString("photoUrl") ?: ""

				val userData = UserDataEntity(key = uid, name = name, id = email, img = photoUrl)

				resultUserDataList.add(userData)
			}

			for (document in emailQuery) {
				val uid = document.getString("uid") ?: ""
				val name = document.getString("name") ?: ""
				val email = document.getString("email") ?: ""
				val photoUrl = document.getString("photoUrl") ?: ""

				val userData = UserDataEntity(key = uid, name = name, id = email, img = photoUrl)


				if (!resultUserDataList.contains(userData)) {
					resultUserDataList.add(userData)
				}
			}

            resultUserDataList

        } catch (e: Exception) {
            Log.e("FirebaseRepo", "UserData 가져오기 중 오류 발생: ${e.message}")
            emptyList()
        }
    }


//Template

	override suspend fun getAllTemplate(user: String): List<TemplateEntity> {
		val database = Firebase.database
		val path = "$user/$TEMPLATE_LIST/TEMPLATE"

		try {
			val snapshot = database.getReference(path).get().await()

			return if (snapshot.exists()) {
				val responseList = mutableListOf<TemplateEntity>()
				for (userSnapshot in snapshot.children) {
					val getData = userSnapshot.getValue(TemplateEntity::class.java)

					getData?.let {

						responseList.add(getData)
					}
				}
				responseList
			} else {
				emptyList()
			}
		} catch (e: Exception) {

			Log.e("FirebaseRepo", "Tag 데이터 가져오기 중 오류 발생")
			return emptyList()
		}
	}

	override suspend fun setTemplate(user: String, item: TemplateEntity): List<TemplateEntity> {
		val database = Firebase.database
		val path = "$user/$TEMPLATE_LIST/TEMPLATE"
		val myRef = database.getReference(path)

		myRef.child(item.id).setValue(item)

		try {
			val snapshot = database.getReference(path).get().await()

			if (snapshot.exists()) {
				val responseList = mutableListOf<TemplateEntity>()
				for (userSnapshot in snapshot.children) {
					val getData = userSnapshot.getValue(TemplateEntity::class.java)

					getData?.let {

						responseList.add(getData)
					}
				}
				return responseList
			} else {
				return emptyList()
			}
		} catch (e: Exception) {

			Log.e("FirebaseRepo", "Tag 데이터 가져오기 중 오류 발생")
			return emptyList()
		}
	}

	override suspend fun setTemplateList(user: String, items: List<TemplateEntity>) {
		val database = Firebase.database
		val path = "$user/$TEMPLATE_LIST/TEMPLATE"
		val myRef = database.getReference(path)
		for (item in items) {
			myRef.child(item.id).setValue(item)
		}

	}

	override suspend fun deleteTemplate(user: String, key: String) {
		val database = Firebase.database
		val myRef = database.getReference("$user/$TEMPLATE_LIST/TEMPLATE")

		myRef.child(key).removeValue()
	}

	override suspend fun updateData(user: String, item: DataEntity) {
		val database = Firebase.database
		val path = "$user/$TEMPLATE_DATA"
		val myRef = database.getReference(path)

		myRef.child(item.id).setValue(item)
	}

	override suspend fun updateDataList(user: String, items: List<DataEntity>) {
		val database = Firebase.database
		val path = "$user/$TEMPLATE_DATA"
		val myRef = database.getReference(path)

		for (item in items) {
			myRef.child(item.id).setValue(item)
		}

	}

	override suspend fun getBackupData(user: String): List<DataEntity> {
		val database = Firebase.database
		val path = "$user/$TEMPLATE_DATA"
		try {
			val snapshot = database.getReference(path).get().await()

			return if (snapshot.exists()) {
				val responseList = mutableListOf<DataEntity>()
				for (userSnapshot in snapshot.children) {
					val getData = userSnapshot.getValue(DataEntity::class.java)

					getData?.let {

						responseList.add(getData)
					}
				}
				responseList
			} else {
				emptyList()
			}
		} catch (e: Exception) {

			Log.e("FirebaseRepo", "BackupData 데이터 가져오기 중 오류 발생")
			return emptyList()
		}
	}

	override suspend fun deleteData(user: String, key: String) {
		val database = Firebase.database
		val path = "$user/$TEMPLATE_DATA"
		val myRef = database.getReference(path)

		myRef.child(key).removeValue()
	}


}
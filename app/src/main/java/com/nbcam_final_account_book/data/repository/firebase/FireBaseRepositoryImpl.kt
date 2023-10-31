package com.nbcam_final_account_book.data.repository.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.data.model.local.DataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.unit.Unit.TEMPLATE_DATA
import com.nbcam_final_account_book.unit.Unit.TEMPLATE_LIST
import kotlinx.coroutines.tasks.await
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

    //Template

    override suspend fun getAllTemplate(user: String): List<TemplateEntity> {
        val database = Firebase.database
        val path = "$user/$TEMPLATE_LIST"

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
        val path = "$user/$TEMPLATE_LIST"
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

    override suspend fun deleteTemplate(user: String, key: String) {
        val database = Firebase.database
        val myRef = database.getReference("$user/$TEMPLATE_LIST")

        myRef.child(key).removeValue()
    }

    override suspend fun updateData(user: String, item: DataEntity) {
        val database = Firebase.database
        val path = "$user/$TEMPLATE_DATA"
        val myRef = database.getReference(path)

        myRef.child(item.id).setValue(item)
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
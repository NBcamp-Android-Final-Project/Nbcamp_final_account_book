package com.nbcam_final_account_book.data.repository.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.remote.ResponseEntryModel
import com.nbcam_final_account_book.data.model.remote.ResponseTagModel
import com.nbcam_final_account_book.persentation.budget.BudgetModel
import com.nbcam_final_account_book.persentation.entry.EntryModel
import com.nbcam_final_account_book.persentation.entry.toResponse
import com.nbcam_final_account_book.persentation.tag.TagModel
import com.nbcam_final_account_book.persentation.tag.toResponse
import com.nbcam_final_account_book.unit.Unit.PATH_BUDGET
import com.nbcam_final_account_book.unit.Unit.PATH_ENTRY
import com.nbcam_final_account_book.unit.Unit.PATH_TAG
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FireBaseRepositoryImpl(

) : FireBaseRepository {
    override fun getUser(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""
    }

    override suspend fun getAllEntry(
        user: String,
        template: String
    ): List<ResponseEntryModel> {

        val database = Firebase.database
        val path = "$user/$template$PATH_ENTRY"

        try {
            val snapshot = database.getReference(path).get().await()

            if (snapshot.exists()) {
                val responseList = mutableListOf<ResponseEntryModel>()
                for (userSnapshot in snapshot.children) {
                    val itemKey = userSnapshot.key ?: ""
                    val getData = userSnapshot.getValue(EntryModel::class.java)

                    getData?.let {
                        val responseEntryModel = getData.toResponse(itemKey)

                        responseList.add(responseEntryModel)
                    }
                }
                return responseList
            } else {
                return emptyList()
            }
        } catch (e: Exception) {

            Log.e("FirebaseRepo", "Template 데이터 가져오기 중 오류 발생")
            return emptyList()
        }
    }

    override suspend fun setEntry(
        user: String,
        template: String,
        item: EntryModel
    ): List<ResponseEntryModel> {

        val database = Firebase.database
        val path = "$user/$template$PATH_ENTRY"
        val myRef = database.getReference(path)

        myRef.push().setValue(item)

        try {
            val snapshot = database.getReference(path).get().await()

            if (snapshot.exists()) {
                val responseList = mutableListOf<ResponseEntryModel>()
                for (userSnapshot in snapshot.children) {
                    val itemKey = userSnapshot.key ?: ""
                    val getData = userSnapshot.getValue(EntryModel::class.java)

                    getData?.let {
                        val responseEntryModel = getData.toResponse(itemKey)

                        responseList.add(responseEntryModel)
                    }
                }
                return responseList
            } else {
                return emptyList()
            }
        } catch (e: Exception) {

            Log.e("FirebaseRepo", "Template 데이터 가져오기 중 오류 발생")
            return emptyList()
        }
    }

    override suspend fun deleteEntry(
        user: String,
        template: String,
        item: ResponseEntryModel
    ): List<ResponseEntryModel> {
        val database = Firebase.database
        val path = "$user/$template$PATH_ENTRY"
        val myRef = database.getReference(path)

        myRef.child(item.key).removeValue()

        try {
            val snapshot = database.getReference(path).get().await()

            if (snapshot.exists()) {
                val responseList = mutableListOf<ResponseEntryModel>()
                for (userSnapshot in snapshot.children) {
                    val itemKey = userSnapshot.key ?: ""
                    val getData = userSnapshot.getValue(EntryModel::class.java)

                    getData?.let {
                        val responseEntryModel = getData.toResponse(itemKey)

                        responseList.add(responseEntryModel)
                    }
                }
                return responseList
            } else {
                return emptyList()
            }
        } catch (e: Exception) {

            Log.e("FirebaseRepo", "Template 데이터 가져오기 중 오류 발생")
            return emptyList()
        }
    }

    override suspend fun getAllTag(
        user: String,
        template: String,
    ): List<ResponseTagModel> {
        val database = Firebase.database
        val path = "$user/$template$PATH_TAG"

        try {
            val snapshot = database.getReference(path).get().await()

            if (snapshot.exists()) {
                val responseList = mutableListOf<ResponseTagModel>()
                for (userSnapshot in snapshot.children) {
                    val itemKey = userSnapshot.key ?: ""
                    val getData = userSnapshot.getValue(TagModel::class.java)

                    getData?.let {
                        val responseEntryModel = getData.toResponse(itemKey)

                        responseList.add(responseEntryModel)
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

    override suspend fun setTag(
        user: String,
        template: String,
        item: TagModel
    ): List<ResponseTagModel> {
        val database = Firebase.database
        val path = "$user/$template$PATH_TAG"
        val myRef = database.getReference(path)

        myRef.push().setValue(item)


        try {
            val snapshot = database.getReference(path).get().await()

            if (snapshot.exists()) {
                val responseList = mutableListOf<ResponseTagModel>()
                for (userSnapshot in snapshot.children) {
                    val itemKey = userSnapshot.key ?: ""
                    val getData = userSnapshot.getValue(TagModel::class.java)

                    getData?.let {
                        val responseEntryModel = getData.toResponse(itemKey)

                        responseList.add(responseEntryModel)
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

    override suspend fun deleteTag(
        user: String,
        template: String,
        item: ResponseTagModel
    ): List<ResponseTagModel> {
        val database = Firebase.database
        val path = "$user/$template$PATH_TAG"
        val myRef = database.getReference(path)

        myRef.child(item.key).removeValue()

        try {
            val snapshot = database.getReference(path).get().await()

            if (snapshot.exists()) {
                val responseList = mutableListOf<ResponseTagModel>()
                for (userSnapshot in snapshot.children) {
                    val itemKey = userSnapshot.key ?: ""
                    val getData = userSnapshot.getValue(TagModel::class.java)

                    getData?.let {
                        val responseEntryModel = getData.toResponse(itemKey)

                        responseList.add(responseEntryModel)
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

    override suspend fun getAllTemplate(user: String): List<TemplateEntity> {
        val database = Firebase.database
        val path = "$user/TemplateList"

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
        val path = "$user/TemplateList"
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


    override suspend fun setBudget(user: String, template: String, budget: BudgetModel) {
        val database = Firebase.database
        val path = "$user/$template/$PATH_BUDGET"
        val myRef = database.getReference(path)

        myRef.push().setValue(budget)
    }

    override suspend fun getBudget(user: String, template: String): List<BudgetModel> {
        val database = Firebase.database
        val path = "$user/$template/$PATH_BUDGET"
        val myRef = database.getReference(path)

        try {
            val snapshot = database.getReference(path).get().await()

            if (snapshot.exists()) {
                val responseList = mutableListOf<BudgetModel>()
                for (userSnapshot in snapshot.children) {
                    val getData = userSnapshot.getValue(BudgetModel::class.java)

                    getData?.let {

                        responseList.add(getData)
                    }
                }
                return responseList
            } else {
                return emptyList()
            }
        } catch (e: Exception) {

            Log.e("FirebaseRepo", "BudgetModel 데이터 가져오기 중 오류 발생")
            return emptyList()
        }
    }

    override fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
    }


}
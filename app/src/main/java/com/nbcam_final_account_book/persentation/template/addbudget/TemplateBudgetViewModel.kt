package com.nbcam_final_account_book.persentation.template.addbudget

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.local.BudgetEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TemplateBudgetViewModel(
    private val roomRepo: RoomRepository,
    private val sharedProvider: SharedProvider,
    private val fireRepo: FireBaseRepository
) : ViewModel() {


    suspend fun insertTemplate(title: String, budget: String) = withContext(Dispatchers.IO) {


        val key = roomRepo.insertFirstTemplate(title)  // room에 데이터 삽입

        val currentTemplate = roomRepo.selectTemplateByKey(key)
        val budgetEntity = BudgetEntity(
            id = 0,
            key = key,
            value = budget
        )
        roomRepo.insertBudget(budgetEntity)

        //firebase
        Log.d("삽입.Template 모델", currentTemplate.toString())
        fireRepo.setTemplate(fireRepo.getUser(), currentTemplate)   // 이후 firebase에 데이터 삽입


        currentTemplate


    }

}

class TemplateBudgetViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateBudgetViewModel::class.java)) {
            return TemplateBudgetViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                ),
                SharedProviderImpl(context),
                FireBaseRepositoryImpl()
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
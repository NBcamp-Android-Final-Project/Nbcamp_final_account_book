package com.nbcam_final_account_book.persentation.template.addbudget

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.data.sharedprovider.SharedProvider
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import kotlinx.coroutines.launch

class TemplateBudgetViewModel(
    private val roomRepo: RoomRepository,
    private val sharedProvider: SharedProvider,
    private val fireRepo: FireBaseRepository
) : ViewModel() {

    private val _liveTemplateEntity: MutableLiveData<TemplateEntity> = MutableLiveData()
    val liveTemplateEntity: LiveData<TemplateEntity> get() = _liveTemplateEntity
    fun insertFirstTemplate(title: String, budget: String) {


        viewModelScope.launch {
            roomRepo.insertFirstTemplate(title)  // room에 데이터 삽입

            val templateModel = roomRepo.selectFirstTemplate()
            _liveTemplateEntity.value = templateModel
            Log.d("현재.모델", templateModel.toString())
            fireRepo.setTemplate(fireRepo.getUser(), templateModel)   // 이후 firebase에 데이터 삽입

            val template = "${templateModel.templateTitle}-${templateModel.id}"
            fireRepo.setBudget(
                user = fireRepo.getUser(),
                template = template,
                budget = budget
            )

        }
    }

    fun getModel(): TemplateEntity? {
        return liveTemplateEntity.value
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
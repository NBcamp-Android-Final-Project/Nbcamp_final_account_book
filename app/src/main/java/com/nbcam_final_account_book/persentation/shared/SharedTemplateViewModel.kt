package com.nbcam_final_account_book.persentation.shared

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import kotlinx.coroutines.launch
import com.nbcam_final_account_book.unit.Unit
import com.nbcam_final_account_book.unit.Unit.SHARED_PATH
import com.nbcam_final_account_book.unit.Unit.TEMPLATE_LIST


class SharedTemplateViewModel(
    private val fireRepo: FireBaseRepository
) : ViewModel() {

    private val _searchResultList : MutableLiveData<List<UserDataEntity>> = MutableLiveData()
    val searchResultList get() = _searchResultList

    fun setFilter(keyword: String) {
        viewModelScope.launch {
            val result = fireRepo.searchUserDataInFireStore(keyword)
            _searchResultList.value = result
        }
    }

    fun sharedTemplate(sharedUid: String, template: TemplateEntity) {
        viewModelScope.launch {
            // 템플릿 타입을 online으로 설정
            val sharedTemplate = template.copy(type = Unit.TEMPLATE_ONLINE)

            // Firebase에 저장 경로를 설정
            val sharedPath = "$sharedUid/$TEMPLATE_LIST/$SHARED_PATH"

            // Firebase에 공유 템플릿 저장
            fireRepo.sharedTemplate(sharedPath, sharedTemplate)
        }
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            val allUsers = fireRepo.getAllUsers()
            _searchResultList.value = allUsers
        }
    }

}

class SharedTemplateViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedTemplateViewModel::class.java)) {
            return SharedTemplateViewModel(
                FireBaseRepositoryImpl()
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
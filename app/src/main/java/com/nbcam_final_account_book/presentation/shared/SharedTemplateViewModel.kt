package com.nbcam_final_account_book.presentation.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepository
import com.nbcam_final_account_book.data.repository.firebase.FireBaseRepositoryImpl
import com.nbcam_final_account_book.unit.Unit
import com.nbcam_final_account_book.unit.Unit.SHARED_PATH
import com.nbcam_final_account_book.unit.Unit.TEMPLATE_LIST
import kotlinx.coroutines.launch


class SharedTemplateViewModel(
	private val fireRepo: FireBaseRepository
) : ViewModel() {

	private val _searchResultList: MutableLiveData<List<UserDataEntity>> = MutableLiveData()
	val searchResultList get() = _searchResultList

	private val _sharedTemplateInfo = MutableLiveData<List<TemplateEntity>>()
	val sharedTemplateInfo: LiveData<List<TemplateEntity>> get() = _sharedTemplateInfo

	init {
		getSharedTemplateInfo()
	}

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

	/**
	 * Firebase RTDB 에서 shared template 를 얻어오는 영역
	 */
	// Firebase RTDB 에서 UID/template_list/SHARED 경로의 데이터 유무 판단
	private fun isSharedEnabled(uid: String): Boolean {
		val database = Firebase.database.reference
		val path = "$uid/$TEMPLATE_LIST/$SHARED_PATH"
		var isSharedEnabled = false

		val sharedListener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				isSharedEnabled = snapshot.exists()
			}

			override fun onCancelled(error: DatabaseError) {
				Log.w("shared", "loadShared:onCancelled", error.toException())
			}
		}
		database.child(path).addValueEventListener(sharedListener)

		return isSharedEnabled
	}

	// Firebase RTDB 에서 공유된 가계부 Info 획득
	private fun getSharedTemplateInfoFromDB(uid: String): MutableList<TemplateEntity> {
		val database = Firebase.database.reference
		val path = "$uid/$TEMPLATE_LIST/$SHARED_PATH"
		val sharedTemplateInfo = mutableListOf<TemplateEntity>()

		val sharedListener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				for (template in snapshot.children) {
					val data = template.getValue(TemplateEntity::class.java)!!

					sharedTemplateInfo.add(data)
				}
			}

			override fun onCancelled(error: DatabaseError) {
				Log.w("shared", "loadShared:onCancelled", error.toException())
			}
		}
		database.child(path).addValueEventListener(sharedListener)

		return sharedTemplateInfo
	}

	// SHARED 에 데이터가 존재할 경우 SHARED/TemplateEntity 를 획득
	private fun getSharedTemplateInfo() {
		val uid = Firebase.auth.currentUser?.uid
		if (uid != null && isSharedEnabled(uid)) {
			_sharedTemplateInfo.value = getSharedTemplateInfoFromDB(uid)
		}
	}
}

class SharedTemplateViewModelFactory : ViewModelProvider.Factory {
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
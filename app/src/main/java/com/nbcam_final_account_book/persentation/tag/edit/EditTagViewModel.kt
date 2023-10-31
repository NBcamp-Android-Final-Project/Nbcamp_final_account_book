package com.nbcam_final_account_book.persentation.tag.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import kotlinx.coroutines.launch

class EditTagViewModel(private val roomRepo: RoomRepository) : ViewModel() {

	fun insertTag(item: TagEntity) {
		viewModelScope.launch {
			roomRepo.insertTag(item)
		}
	}

	fun updateTag(item: TagEntity, title: String) {
		viewModelScope.launch {
			val modifiedTag = item.copy(title = title)

			roomRepo.updateTag(modifiedTag)
		}
	}
}

class EditTagViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return if (modelClass.isAssignableFrom(EditTagViewModel::class.java)) {
			EditTagViewModel(roomRepo = RoomRepositoryImpl(AndroidRoomDataBase.getInstance(context = context))) as T
		} else {
			throw IllegalArgumentException("Not found ViewModel class.")
		}
	}
}

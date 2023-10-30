package com.nbcam_final_account_book.persentation.tag

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.main.MainViewModel

class TagViewModel(private val roomRepo: RoomRepository) : ViewModel() {

	val liveTagListInEdit: LiveData<List<TagEntity>> = MainViewModel.liveKey.switchMap { key ->
		roomRepo.getLiveTagByKey(key)
	}

	fun getTagListAll(): MutableList<TagEntity> {
		val list = liveTagListInEdit.value.orEmpty().toMutableList()
		return list
	}
}

class TagViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
			TagViewModel(RoomRepositoryImpl(AndroidRoomDataBase.getInstance(context))) as T
		} else {
			throw IllegalArgumentException("Not found ViewModel class.")
		}
	}
}
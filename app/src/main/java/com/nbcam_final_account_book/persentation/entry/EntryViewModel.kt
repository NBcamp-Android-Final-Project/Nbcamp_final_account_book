package com.nbcam_final_account_book.persentation.entry

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.main.MainViewModel
import kotlinx.coroutines.launch

class EntryViewModel(private val roomRepo: RoomRepository) : ViewModel() {

	private val _liveValue: MutableLiveData<String?> = MutableLiveData()
	val liveValue: LiveData<String?> get() = _liveValue

	private val _liveType: MutableLiveData<String?> = MutableLiveData()
	val liveType: LiveData<String?> get() = _liveType

	private val _currentEntry = MutableLiveData<EntryEntity>()
	val currentEntry: LiveData<EntryEntity> get() = _currentEntry

	// EntryEntity 업데이트
	fun updateEntryEntity(entry: EntryEntity) {
		_currentEntry.value = entry
	}

	//CurrentTemplateData
	private val _entryLiveCurrentTemplate: MutableLiveData<TemplateEntity?> = MutableLiveData()
	val entryLiveCurrentTemplate: LiveData<TemplateEntity?> get() = _entryLiveCurrentTemplate

	private var _category = MutableLiveData<String>()
	val category: LiveData<String> get() = _category

	private var _categoryDrawable = MutableLiveData<Int>()
	val categoryDrawable: LiveData<Int> get() = _categoryDrawable

	val liveTagList: LiveData<List<TagEntity>>
		get() = MainViewModel.liveKey.switchMap { key ->
			roomRepo.getLiveTagByKey(key)
		}

	fun updateCurrentTemplateEntry(item: TemplateEntity?) {
		if (item == null) return
		_entryLiveCurrentTemplate.value = item
	}

	fun getCurrentTemplateEntry(): TemplateEntity? {
		return entryLiveCurrentTemplate.value
	}

	fun updateAmount(amount: String, type: String) {
		_liveValue.value = amount
		_liveType.value = type
		Log.d("현재 값", liveValue.value.toString())
		Log.d("현재 타입", liveType.value.toString())
	}

	fun deleteTagById(item: TagEntity) {
		viewModelScope.launch {
			roomRepo.deleteTagById(item.id)
		}
	}

	fun getData(): Pair<String?, String?> {
		val currentValue = liveValue.value?.toString()
		val currentType = liveType.value?.toString()
		return Pair(currentValue, currentType)
	}

	fun insertEntity(item: EntryEntity) {
		viewModelScope.launch {
			roomRepo.insertEntry(item)
		}

	}

	fun updateEntity(item: EntryEntity) {
		viewModelScope.launch {
			roomRepo.updateEntry(item)
		}
	}

	fun setCategory(text: String) {
		_category.value = text
	}

	fun setCategoryDrawable(drawable: Int) {
		_categoryDrawable.value = drawable
	}
}

class EntryViewModelFactory(
	private val context: Context
) : ViewModelProvider.Factory {

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
			return EntryViewModel(
				RoomRepositoryImpl(
					AndroidRoomDataBase.getInstance(context)
				)
			) as T
		} else {
			throw IllegalArgumentException("Not found ViewModel class.")
		}
	}

}
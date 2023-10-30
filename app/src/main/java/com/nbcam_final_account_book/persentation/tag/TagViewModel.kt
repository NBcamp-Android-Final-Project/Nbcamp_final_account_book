package com.nbcam_final_account_book.persentation.tag

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase
import com.nbcam_final_account_book.persentation.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagViewModel(private val roomRepo: RoomRepository) : ViewModel() {

    val liveTagListInEdit: LiveData<List<TagEntity>> = MainViewModel.liveKey.switchMap { key ->
        roomRepo.getLiveTagByKey(key)
    }

    fun getTagListAll(): MutableList<TagEntity> {
        val list = liveTagListInEdit.value.orEmpty().toMutableList()
        return list
    }

    fun updateTag(item:TagEntity,title:String){
        viewModelScope.launch {
            val newITem = item.copy(title = title)

            roomRepo.updateTag(newITem)
        }
    }


    fun tagUpdateInEdit(items: List<TagEntity>) {
        viewModelScope.launch {
            var index = 0
            val list = items.map { it.copy(order = index++) }
            roomRepo.deleteTagByKey(MainViewModel.liveKey.value)
            roomRepo.insertTagList(list)
        }



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
package com.nbcam_final_account_book.persentation.dialog.template

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.repository.room.RoomRepository
import com.nbcam_final_account_book.data.repository.room.RoomRepositoryImpl
import com.nbcam_final_account_book.data.room.AndroidRoomDataBase

class TemplateDialogViewModel(
    private val roomRepo: RoomRepository
) : ViewModel() {

    val dialogLiveTemplateList: LiveData<List<TemplateEntity>> get() = roomRepo.getAllLiveTemplate()

    fun getTemplateSizeInTemplateDialog(): Boolean {
        return dialogLiveTemplateList.value.orEmpty().size < 3
    }

}

class TemplateDialogViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateDialogViewModel::class.java)) {
            return TemplateDialogViewModel(
                RoomRepositoryImpl(
                    AndroidRoomDataBase.getInstance(context)
                ),
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
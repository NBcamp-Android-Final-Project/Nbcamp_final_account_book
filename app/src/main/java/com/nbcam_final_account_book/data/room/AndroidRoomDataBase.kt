package com.nbcam_final_account_book.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nbcam_final_account_book.data.model.local.TemplateDataEntity
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.data.room.dao.TemplateDao
import com.nbcam_final_account_book.data.room.dao.TemplateDataDao

@Database(entities = [TemplateEntity::class], [TemplateDataEntity::class], version = 3)
abstract class AndroidRoomDataBase : RoomDatabase() {

    abstract fun templateDao(): TemplateDao
    abstract fun templateDataDao(): TemplateDataDao

    companion object {
        @Volatile
        private var INSTANCE: AndroidRoomDataBase? = null

        fun getInstance(
            context: Context
        ): AndroidRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AndroidRoomDataBase::class.java,
                    "android_room_database_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

}
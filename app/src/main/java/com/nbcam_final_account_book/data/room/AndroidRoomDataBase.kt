package com.nbcam_final_account_book.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nbcam_final_account_book.data.model.local.TemplateEntity

@Database(entities = [TemplateEntity::class], version = 1)
abstract class AndroidRoomDataBase : RoomDatabase() {

    abstract fun templateDao(): TemplateDao

    companion object {
        @Volatile
        private var INSTANCE: AndroidRoomDataBase? = null

        fun getInstance(
            context:Context
        ):AndroidRoomDataBase{
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
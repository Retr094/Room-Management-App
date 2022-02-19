package com.pcandroiddev.roommanagementapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.pcandroiddev.roommanagementapp.models.Room


@Database(entities = [Room::class], version = 1, exportSchema = false)
abstract class RoomDb: RoomDatabase() {

    abstract fun getRoomDao(): RoomDao

    companion object{
        @Volatile
        private var instance: RoomDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it  }
        }

        private fun createDatabase(context: Context) = androidx.room.Room.databaseBuilder(
            context,
            RoomDb::class.java,
            "room_database"
        ).build()

    }


}
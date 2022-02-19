package com.pcandroiddev.roommanagementapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pcandroiddev.roommanagementapp.models.Room
import kotlinx.coroutines.flow.Flow


@Dao
interface RoomDao {

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): Flow<List<Room>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: Room)

    @Delete
    suspend fun deleteRoom(room: Room)

    @Query("SELECT * FROM rooms WHERE roomNumber = :roomNumber")
    fun getRoomsByRoomNumber(roomNumber: Int): List<Room>



}
package com.pcandroiddev.roommanagementapp.repository

import com.pcandroiddev.roommanagementapp.db.RoomDb
import com.pcandroiddev.roommanagementapp.models.Room

class RoomRepository(
    private val db: RoomDb
) {

    suspend fun insertRoom(room: Room) = db.getRoomDao().insertRoom(room)

    fun getAllRooms() = db.getRoomDao().getAllRooms()

    suspend fun deleteRoom(room: Room) = db.getRoomDao().deleteRoom(room)

    fun getRoomsByRoomNumber(roomNumber: Int): List<Room>  = db.getRoomDao().getRoomsByRoomNumber(roomNumber)
}
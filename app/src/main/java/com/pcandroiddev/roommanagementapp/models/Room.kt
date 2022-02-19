package com.pcandroiddev.roommanagementapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "rooms")
data class Room(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerName: String,
    val checkInDate: String,
    val checkoutDate: String,
    val checkInTime: String,
    val checkoutTime: String,
    val roomNumber: Int
)

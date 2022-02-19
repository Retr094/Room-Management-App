package com.pcandroiddev.roommanagementapp.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.roommanagementapp.models.Room
import com.pcandroiddev.roommanagementapp.repository.RoomRepository
import com.pcandroiddev.roommanagementapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class BookRoomVM(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _bookRoom: MutableLiveData<Resource<String>> = MutableLiveData()
    val bookRoom: LiveData<Resource<String>> = _bookRoom


    @RequiresApi(Build.VERSION_CODES.O)
    fun insertRoom(newRoom: Room) = viewModelScope.launch(Dispatchers.IO) {
        /*
        RESOURCE LOADING MESSAGE
         */
        _bookRoom.postValue(Resource.Loading())

        val existingRooms = roomRepository.getRoomsByRoomNumber(newRoom.roomNumber)

        if (existingRooms.isEmpty()) {
            /*
            BOOK ROOM
            POST LIVEDATA RESOURCE SUCCESS MESSAGE
             */
            roomRepository.insertRoom(newRoom)
            _bookRoom.postValue(Resource.Success("Room Booked"))

        } else /*if (existingRooms.isNotEmpty())*/ {


            for (existingRoom in existingRooms) {

                val existingCheckInTime = LocalTime.parse(existingRoom.checkInTime)
                val existingCheckOutTime = LocalTime.parse(existingRoom.checkoutTime)
                val newCheckInTime = LocalTime.parse(newRoom.checkInTime)
                val newCheckOutTime = LocalTime.parse(newRoom.checkoutTime)

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val existingCheckInDate = LocalDate.parse(existingRoom.checkInDate, formatter)
                val existingCheckOutDate = LocalDate.parse(existingRoom.checkoutDate, formatter)
                val newCheckInDate = LocalDate.parse(newRoom.checkInDate, formatter)
                val newCheckOutDate = LocalDate.parse(newRoom.checkoutDate, formatter)



                if (newCheckOutDate.isBefore(existingCheckInDate) || newCheckInDate.isAfter(
                        existingCheckOutDate
                    )
                ) {
                    /*
                  BOOK ROOM
                   */
                    roomRepository.insertRoom(newRoom)
                    _bookRoom.postValue(Resource.Success("Room Booked"))
                    Log.d("RoomVM", "Case 1, id: ${existingRoom.id}")
                    return@launch

                } else if (newCheckOutDate.isEqual(existingCheckInDate) && newCheckInDate.isBefore(
                        existingCheckInDate
                    ) && newCheckOutDate.isBefore(existingCheckOutDate) && newCheckOutTime.isBefore(
                        existingCheckInTime
                    )
                ) {
                    /*
                  BOOK ROOM
                   */
                    roomRepository.insertRoom(newRoom)
                    _bookRoom.postValue(Resource.Success("Room Booked"))
                    Log.d("RoomVM", "Case 2, id: ${existingRoom.id}")
                    return@launch

                } else if (newCheckInDate.isEqual(existingCheckOutDate) && newCheckOutDate.isAfter(
                        existingCheckOutDate
                    ) && newCheckInDate.isAfter(existingCheckInDate) && newCheckInTime.isAfter(
                        existingCheckOutTime
                    )
                ) {
                    /*
                  BOOK ROOM
                   */
                    roomRepository.insertRoom(newRoom)
                    _bookRoom.postValue(Resource.Success("Room Booked"))
                    Log.d("RoomVM", "Case 3, id: ${existingRoom.id}")
                    return@launch

                } else if (newCheckInDate.isEqual(existingCheckInDate) && newCheckInDate.isEqual(
                        existingCheckOutDate
                    ) && newCheckOutDate.isEqual(existingCheckInDate) && newCheckOutDate.isEqual(
                        existingCheckOutDate
                    )
                ) {
                    if (newCheckInTime.isAfter(existingCheckOutTime) || newCheckOutTime.isBefore(
                            existingCheckInTime
                        )
                    ) {
                        /*
                            BOOK ROOM
                        */
                        roomRepository.insertRoom(newRoom)
                        _bookRoom.postValue(Resource.Success("Room Booked"))
                        Log.d("RoomVM", "Case 4, id: ${existingRoom.id}")
                        return@launch
                    } else {
                        /*
                        DON'T BOOK
                        POST LIVEDATA RESOURCE ERROR MESSAGE WITH message AS "ROOM NOT AVAILABLE"
                         */
                        _bookRoom.postValue(Resource.Error("Room Not Available"))
                        Log.d("RoomVM", "Case 4, id: ${existingRoom.id}")
                        Log.d("RoomVM", "4th Case, id: ${existingRoom.id}")
                        return@launch
                    }
                } else {
                    /*
                    DON'T BOOK
                    POST LIVEDATA RESOURCE ERROR MESSAGE WITH message AS "ROOM NOT AVAILABLE"
                     */
                    _bookRoom.postValue(Resource.Error("Room Not Available"))
                    Log.d("RoomVM", "Case 5, id: ${existingRoom.id}")
                    Log.d("RoomVM", "5th Case, id: ${existingRoom.id}")
                    return@launch
                }


            }
        }
    }


}
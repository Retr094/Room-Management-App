package com.pcandroiddev.roommanagementapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.roommanagementapp.models.Room
import com.pcandroiddev.roommanagementapp.repository.RoomRepository
import com.pcandroiddev.roommanagementapp.util.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeVM(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _rooms: MutableLiveData<Resource<List<Room>>> = MutableLiveData()
    val rooms: LiveData<Resource<List<Room>>> = _rooms


    init {
        getAllRooms()
    }


    private fun getAllRooms() {
        viewModelScope.launch {
            _rooms.postValue(Resource.Loading())

            roomRepository.getAllRooms().collect {
                _rooms.postValue(Resource.Success(it))
            }

        }
    }


    fun deleteRoom(room: Room) {
        viewModelScope.launch {
            roomRepository.deleteRoom(room)
        }
    }




}
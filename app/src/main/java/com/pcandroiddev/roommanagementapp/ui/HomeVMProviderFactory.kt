package com.pcandroiddev.roommanagementapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pcandroiddev.roommanagementapp.repository.RoomRepository

class HomeVMProviderFactory(
    private val roomRepository: RoomRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeVM(roomRepository) as T
    }
}
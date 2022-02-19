package com.pcandroiddev.roommanagementapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.pcandroiddev.roommanagementapp.databinding.ActivityMainBinding
import com.pcandroiddev.roommanagementapp.db.RoomDb
import com.pcandroiddev.roommanagementapp.repository.RoomRepository
import com.pcandroiddev.roommanagementapp.ui.BookRoomVM
import com.pcandroiddev.roommanagementapp.ui.HomeVM
import com.pcandroiddev.roommanagementapp.ui.BookRoomVMProviderFactory
import com.pcandroiddev.roommanagementapp.ui.HomeVMProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bookRoomVM: BookRoomVM
    lateinit var homeVM: HomeVM
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        val roomRepository = RoomRepository(RoomDb(this))
        val viewModelProviderFactory = BookRoomVMProviderFactory(roomRepository)
        val homeVmProviderFactory = HomeVMProviderFactory(roomRepository)
        bookRoomVM = ViewModelProvider(this,viewModelProviderFactory).get(BookRoomVM::class.java)
        homeVM = ViewModelProvider(this, homeVmProviderFactory).get(HomeVM::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()

    }
}
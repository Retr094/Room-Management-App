package com.pcandroiddev.roommanagementapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pcandroiddev.roommanagementapp.MainActivity
import com.pcandroiddev.roommanagementapp.R
import com.pcandroiddev.roommanagementapp.adapter.RoomsAdapter
import com.pcandroiddev.roommanagementapp.databinding.FragmentHomeBinding
import com.pcandroiddev.roommanagementapp.util.Resource

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: HomeVM
    lateinit var roomsAdapter: RoomsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).homeVM
        setupRecyclerView()

        binding.fabBookRoom.setOnClickListener {
            Log.d("HomeFragment", "FAB: FAB CLICKED")
            findNavController().navigate(R.id.action_homeFragment_to_bookRoomFragment)

        }

        viewModel.rooms.observe(viewLifecycleOwner) { data ->
            when (data) {
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                    hideProgressBar()
                    if (data.data?.isEmpty() == true) {
                        binding.tvNoBookedRooms.visibility = View.VISIBLE
                        roomsAdapter.differ.submitList(emptyList())
                    } else {
                        binding.tvNoBookedRooms.visibility = View.GONE
                        data.data?.let {
                            roomsAdapter.differ.submitList(it)
                        }

                    }


                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(activity, data.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }

        val itemTouchHelper  = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val roomItem = roomsAdapter.differ.currentList[position]
                viewModel.deleteRoom(roomItem)
                Snackbar.make(view,"Room Unbooked Successfully", Snackbar.LENGTH_LONG).show()

            }

        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.rvBookedRooms)
        }




    }

    private fun setupRecyclerView() {
        roomsAdapter = RoomsAdapter()
        binding.rvBookedRooms.apply {
            adapter = roomsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
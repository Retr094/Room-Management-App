package com.pcandroiddev.roommanagementapp.ui

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pcandroiddev.roommanagementapp.MainActivity
import com.pcandroiddev.roommanagementapp.R
import com.pcandroiddev.roommanagementapp.databinding.FragmentBookRoomBinding
import com.pcandroiddev.roommanagementapp.models.Room
import com.pcandroiddev.roommanagementapp.util.Resource
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates

class BookRoomFragment : Fragment(R.layout.fragment_book_room) {

    private var _binding: FragmentBookRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var mCustomerName: String
    private lateinit var mStartDate: String
    private lateinit var mEndDate: String
    private lateinit var mStartTime: String
    private lateinit var mEndTime: String
    private var mRoomNumber by Delegates.notNull<Int>()

    lateinit var viewModel: BookRoomVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookRoomBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BookRoomFragment", "onViewCreated: BookRoomFragment Created ")

        viewModel = (activity as MainActivity).bookRoomVM
        setupDropDownArrayAdapter()

        binding.tiedCustomerName.doOnTextChanged { text, _, _, _ ->
            if (text!!.isEmpty()) {
                binding.tilCustomerName.error = "Please enter customer name!"
            } else if (text.isNotEmpty()) {
                binding.tilCustomerName.error = null
            }
        }

        binding.tiedDateRange.setOnClickListener {
            closeKeyBoard(requireActivity())
            showDateRangePicker()
        }

        binding.tiedStartTime.setOnClickListener {
            closeKeyBoard(requireActivity())
            openCheckInTimePicker()
        }

        binding.tiedEndTime.setOnClickListener {
            closeKeyBoard(requireActivity())
            openCheckoutTimePicker()
        }

        binding.actvRoomNo.setOnClickListener {
            closeKeyBoard(requireActivity())
        }

        binding.btnBookRoom.setOnClickListener {

            mCustomerName = binding.tiedCustomerName.text.toString()
            mRoomNumber = binding.actvRoomNo.text.toString().toInt()
            val newRoom = Room(
                customerName = mCustomerName,
                checkInDate = mStartDate,
                checkInTime = mStartTime,
                checkoutDate = mEndDate,
                checkoutTime = mEndTime,
                roomNumber = mRoomNumber
            )
            viewModel.insertRoom(newRoom)
            Log.d("BookRoomFragment", "onViewCreated: Book Button Clicked")

        }



        viewModel.bookRoom.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> binding.btnBookRoom.text = "Checking Availability..."
                is Resource.Success -> {
                    binding.btnBookRoom.text = "Book Room"
                    Snackbar.make(view,"${response.data}",Snackbar.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_bookRoomFragment_to_homeFragment)
                }
                is Resource.Error -> {
                    binding.btnBookRoom.text = "Book Room"
                    Snackbar.make(view,"${response.message}",Snackbar.LENGTH_LONG).show()
                }

            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun openCheckInTimePicker() {

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Set Check In Time")
            .build()
        picker.show(parentFragmentManager, "check_in_time_picker")

        picker.addOnPositiveButtonClickListener {

            val hour = if (picker.hour < 10) {
                "0${picker.hour}"
            } else {
                picker.hour
            }

            val minute = if (picker.minute < 10) {
                "0${picker.minute}"
            } else {
                picker.minute
            }


            val time = "$hour:$minute"
            val checkInTime = LocalTime.parse(time).format(DateTimeFormatter.ofPattern("hh:mm a"))
            mStartTime = time


            binding.tiedStartTime.setText(checkInTime)
            Log.d("MaterialTimePicker", "openCheckInTimePicker:$checkInTime") // 12 hr => 01:00 PM
            Log.d("MaterialTimePicker", "openCheckInTimePicker:$time") // 24 hr => 13:00
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openCheckoutTimePicker() {

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Set Check Out Time")
            .build()
        picker.show(parentFragmentManager, "check_out_time_picker")

        picker.addOnPositiveButtonClickListener {
            val hour = if (picker.hour < 10) {
                "0${picker.hour}"
            } else {
                picker.hour
            }

            val minute = if (picker.minute < 10) {
                "0${picker.minute}"
            } else {
                picker.minute
            }


            val time = "$hour:$minute"
            val checkOutTime = LocalTime.parse(time).format(DateTimeFormatter.ofPattern("hh:mm a"))
            mEndTime = time


            binding.tiedEndTime.setText(checkOutTime)
            Log.d("MaterialTimePicker", "openCheckOutTimePicker:$checkOutTime ")

        }
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Select Duration of Stay")
            .build()

        dateRangePicker.show(parentFragmentManager, "date_range_picker")

        dateRangePicker.addOnPositiveButtonClickListener { datesSelected ->
            val startDate = datesSelected.first
            val endDate = datesSelected.second

            if (startDate != null && endDate != null) {
                val checkInDate = convertLongToTime(startDate)
                val checkoutDate = convertLongToTime(endDate)
                mStartDate = checkInDate
                mEndDate = checkoutDate
                binding.tiedDateRange.setText("$checkInDate - $checkoutDate")
                Log.d(
                    "MaterialDateRangePicker",
                    "showDateRangePicker: $checkInDate - $checkoutDate"
                ) //17/02/2022 - 18/02/2022
            }

        }
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )
        return format.format(date)
    }


    private fun setupDropDownArrayAdapter() {
        val rooms = resources.getStringArray(R.array.room_numbers)
        val dropDownArrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, rooms)
        binding.actvRoomNo.setAdapter(dropDownArrayAdapter)
    }

    private fun closeKeyBoard(activity: Activity) {
        if (activity.currentFocus == null) {
            return
        }
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.pcandroiddev.roommanagementapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pcandroiddev.roommanagementapp.databinding.ItemRoomBinding
import com.pcandroiddev.roommanagementapp.models.Room
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RoomsAdapter(
//    private val rooms: List<Room>
) : RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
//        holder.bind(rooms[position])
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

//    fun getRoomsList(): List<Room> {
//        return rooms
//    }

//    fun updateRoomsList(updatedRooms: ArrayList<Room>){
//        rooms.clear()
//        rooms.addAll(updatedRooms)
//        notifyDataSetChanged()
//    }

    inner class RoomViewHolder(private val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

            @RequiresApi(Build.VERSION_CODES.O)
            fun bind(room: Room){
                binding.apply {
                    tvCustomerName.text = room.customerName
                    tvRoomNo.text = room.roomNumber.toString()
                    tvChekInDate.text = room.checkInDate
                    val checkInTime = LocalTime.parse(room.checkInTime).format(DateTimeFormatter.ofPattern("hh:mm a"))
                    tvChekInTime.text = checkInTime
                    tvChekOutDate.text = room.checkoutDate
                    val checkOutTime = LocalTime.parse(room.checkoutTime).format(DateTimeFormatter.ofPattern("hh:mm a"))
                    tvChekOutTime.text = checkOutTime
                }
            }
    }

}
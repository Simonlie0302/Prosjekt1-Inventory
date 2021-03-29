package com.ikt205.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ikt205.inventory.data.ListDepositoryManager
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.DetailslayoutBinding

class DetailRecyclerAdapter(private var itemList: MutableList<Todo.Item>) :
    RecyclerView.Adapter<DetailRecyclerAdapter.Viewholder>() {

    inner class Viewholder(val binding: DetailslayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Todo.Item) {
            val position: Int = getAdapterPosition()

            binding.detailsItemNameTv.text = item.itemName
            binding.itemCheckbox.isChecked = item.completed
            binding.itemCheckbox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                flip(position)
            }
            binding.itemDelete.setOnClickListener { deleteItem(position) }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            DetailslayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateCollection(newList: MutableList<Todo.Item>) {
        itemList = newList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        lateinit var dialog: AlertDialog
        itemList.removeAt(position)
        updateCollection(itemList)
    }

    fun flip(position: Int) {
        itemList[position].flipStatus()
    }
}


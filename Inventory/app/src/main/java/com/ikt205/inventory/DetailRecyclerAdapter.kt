package com.ikt205.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.DetailslayoutBinding

class DetailRecyclerAdapter(private var item: MutableList<Todo.Item>):RecyclerView.Adapter<DetailRecyclerAdapter.Viewholder>() {


    inner class Viewholder(val binding: DetailslayoutBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Todo.Item) {
            var position: Int = getAdapterPosition()

            binding.detailsItemNameTv.text = item.itemName
            binding.itemCheckbox.isChecked = item.completed
            binding.itemCheckbox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                item.flipStatus()
                }
            binding.itemDelete.setOnClickListener{deleteItem(position)}
        }
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(item[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(DetailslayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun updateCollection(newList: MutableList<Todo.Item>){
        item = newList
        notifyDataSetChanged()
    }

    fun deleteItem(position:Int) {
        lateinit var dialog: AlertDialog
        item.removeAt(position)
        //ListDepositoryManager.instance.deleteItem(position)
        updateCollection(item)
    }

    fun flip(position: Int) {
        item[position].flipStatus()
        updateCollection(item)
    }


}


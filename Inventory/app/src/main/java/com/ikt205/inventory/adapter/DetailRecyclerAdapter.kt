package com.ikt205.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.ikt205.inventory.data.ListDepositoryManager
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.DetailsRecycledLayoutBinding

class DetailRecyclerAdapter(
    private var itemList: MutableList<Todo.Item>,
    title: String,
    private val updateProgress: () -> Unit
) :
    RecyclerView.Adapter<DetailRecyclerAdapter.Viewholder>() {
    var title: String = title

    inner class Viewholder(val binding: DetailsRecycledLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Todo.Item) {
            val position: Int = getAdapterPosition()

            // Binds xml buttons with the data class item.
            binding.detailsItemTitle.text = item.itemName
            binding.itemCheckbox.isChecked = item.completed
            binding.itemCheckbox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                flip(item)
                updateProgress()
            }
            binding.itemDeleteBtn.setOnClickListener {
                deleteItem(position, item)
                updateProgress()
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            DetailsRecycledLayoutBinding.inflate(
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

    fun deleteItem(position: Int, specificItem: Todo.Item) {
        itemList.removeAt(position)
        updateCollection(this.itemList)
        // Using the list depository manager to delete the instance with matching title and the specific item name
        ListDepositoryManager.instance.deleteItem(title, specificItem.itemName)
    }

    fun flip(item: Todo.Item) {
        ListDepositoryManager.instance.flipItemStatus(title, item, item.completed)
    }
}


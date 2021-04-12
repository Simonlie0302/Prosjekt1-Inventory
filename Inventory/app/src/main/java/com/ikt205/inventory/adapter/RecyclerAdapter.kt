package com.ikt205.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikt205.inventory.data.ListDepositoryManager
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.ListlayoutBinding

class ListRecyclerAdapter(
    private var todo: List<Todo>,
    private val onListClicked: (todo: Todo) -> Unit
) : RecyclerView.Adapter<ListRecyclerAdapter.Viewholder>() {

    inner class Viewholder(val binding: ListlayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            val position: Int = getAdapterPosition()

            binding.todoTitle.text = todo.title
            binding.todoProgress.max = todo.getSize()
            binding.todoProgress.setProgress(todo.getCompleted(), true)
            binding.todoProgress.max = todo.getSize()
            binding.deleteTodoBtn.setOnClickListener { deleteItem(position) }
        }
    }

    override fun getItemCount(): Int = todo.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(todo[position])
        holder.itemView.setOnClickListener {
            onListClicked(
                todo[position]
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            ListlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateCollection(newList: List<Todo>) {
        todo = newList as MutableList<Todo>
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        ListDepositoryManager.instance.deleteTodo(position)
        updateCollection(todo)
    }
}
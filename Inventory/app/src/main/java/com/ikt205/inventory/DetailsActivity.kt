package com.ikt205.inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikt205.inventory.adapter.DetailRecyclerAdapter
import com.ikt205.inventory.data.ListDepositoryManager
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.ActivityDetailsBinding
import com.ikt205.inventory.databinding.DetailslayoutBinding
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_details.*


private val TAG: String = "Inventory:MainActivity"

class DetailsActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var bindingTest: DetailslayoutBinding
    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todo = ListHolder.PickedTodo!!
        binding.detailsCardListing.layoutManager = LinearLayoutManager(this)
        binding.detailsCardListing.adapter =
            DetailRecyclerAdapter(todo.itemList, todo.title, this::updateProgress)

        if (!this::bindingTest.isInitialized) {
            bindingTest = DetailslayoutBinding.inflate(layoutInflater)
        }

        setSupportActionBar(toolbar)
        updateProgress()

        // show center aligned title and sub title
        supportActionBar?.apply {
            toolbarTitle.text = todo.title.toString()
            toolbarSubTitle.text = ""
            binding.pbProgressTest.isVisible = true
            title = ""
            this.elevation = 15F
        }

        binding.fabAddItem.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Enter name of todo/item")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            builder.setView(dialogLayout)
            updateProgress()
            builder.setPositiveButton("OK") { dialogInterface, i ->
                addItem(
                    todo,
                    Todo.Item(
                        inputText.text.toString(),
                        false
                    )
                )
            }
            builder.show()
        }

        binding.fabGoBack.setOnClickListener {
            ListHolder.PickedTodo = todo
            Log.e(TAG, "Pushed card : >${todo.toString()}")
            // bindingTest.updateCollection(newList)
            ListDepositoryManager.instance.reloadProgressBar()

            // Skal jeg bruke startActivity(intent) eller finishAndRemoveTask() her
            finishAndRemoveTask()
        }
    }

    fun updateProgress() {
        pbProgressTest.max = todo.getSize()
        pbProgressTest.setProgress(todo.getCompleted(), true)
    }

    private fun addItem(todo: Todo, item: Todo.Item) {
        if (item.itemName.length > 0) {
            ListDepositoryManager.instance.addItem(todo, item)
        } else {
            Toast.makeText(applicationContext, "Title can't be blank!", Toast.LENGTH_LONG).show()
        }
    }
}
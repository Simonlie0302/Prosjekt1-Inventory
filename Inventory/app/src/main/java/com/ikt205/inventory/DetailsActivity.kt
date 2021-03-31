package com.ikt205.inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikt205.inventory.adapter.DetailRecyclerAdapter
import com.ikt205.inventory.data.ListDepositoryManager
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.ActivityDetailsBinding
import com.ikt205.inventory.databinding.DetailslayoutBinding
import kotlinx.android.synthetic.main.action_bar.*


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
        binding.detailsCardListing.adapter = DetailRecyclerAdapter(todo.itemList)

        if (!this::bindingTest.isInitialized) {
            bindingTest = DetailslayoutBinding.inflate(layoutInflater)
        }

//        if(ListHolder.PickedTodo != null){
//            todo = ListHolder.PickedTodo
//            Log.i("Details view", receivedBook.toString())
//        } else{
//
//            setResult(RESULT_CANCELED, Intent(EXTRA_BOOK_INFO).apply {
//                //leg til info vi vil sende tilbake til Main
//            })
//
//            finish()
//        }
//        title=todo.title.toString()

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

        binding.refreshProgress.setOnClickListener {
            updateProgress()
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

    fun updateProgress(){
        binding.pbProgressTest.max = todo.getSize()
        binding.pbProgressTest.setProgress(todo.getCompleted(), true)
    }

    private fun addItem(todo: Todo, item: Todo.Item) {
        ListDepositoryManager.instance.addItem(todo, item)
    }

    companion object {
        val instance = DetailsActivity()
    }
}
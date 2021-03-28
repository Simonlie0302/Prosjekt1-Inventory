package com.ikt205.inventory

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikt205.inventory.data.ListDepositoryManager
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.listlayout.view.*

private val TAG: String = "Inventory:MainActivity"

class ListHolder {

    companion object {
        var PickedTodo: Todo? = null
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardListing.layoutManager = LinearLayoutManager(this)
        binding.cardListing.adapter = ListRecyclerAdapter(emptyList<Todo>(), this::onListClicked)


        ListDepositoryManager.instance.onList = {
            (binding.cardListing.adapter as ListRecyclerAdapter).updateCollection(it)
        }
        ListDepositoryManager.instance.load("s", this)

        //Floating action button
        binding.fabAdd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Enter name of list")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                addTodo(
                    Todo(
                        inputText.text.toString(),
                        mutableListOf(),
                    )
                )
            }
            builder.show()
        }
    }

    private fun addTodo(item: Todo) {
        ListDepositoryManager.instance.addTodo(item)
    }

    private fun onListClicked(todo: Todo): Unit {
        ListHolder.PickedTodo = todo
        Log.e(TAG, "Pushed card : >${todo.toString()}")
        //println("Content : %s", )
        val intent = Intent(this, DetailsActivity::class.java)
        //intent.putExtra("POS", position as Serializable)
        startActivity(intent)
    }

    private fun deleteItem(position: Int) {
        ListDepositoryManager.instance.deleteTodo(position)
    }

}


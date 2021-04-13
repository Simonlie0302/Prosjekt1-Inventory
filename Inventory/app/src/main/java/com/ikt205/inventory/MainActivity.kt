package com.ikt205.inventory

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikt205.inventory.adapter.ListRecyclerAdapter
import com.ikt205.inventory.data.ListDepositoryManager
import com.ikt205.inventory.data.Todo
import com.ikt205.inventory.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_main.*

private val TAG: String = "Inventory:MainActivity"

class ListHolder {
    companion object {
        var PickedTodo: Todo? = null
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardListing.layoutManager = LinearLayoutManager(this)
        binding.cardListing.adapter = ListRecyclerAdapter(emptyList<Todo>(), this::onListClicked)

        // set toolbar as support action bar
        setSupportActionBar(toolbar)

        // show center aligned title and sub title
        supportActionBar?.apply {
            toolbarTitle.text = "Inventory"
            toolbarSubTitle.text = "Never forget anything again"
            title = ""
            this.elevation = 15F
        }

        // Using the listdepo manager to bind the cardListing to the adapter as ListRecyclerAdapter
        ListDepositoryManager.instance.onList = {
            (binding.cardListing.adapter as ListRecyclerAdapter).updateCollection(it)
        }
        ListDepositoryManager.instance.load("s", this)

        //Floating action button
        binding.fabAddTodo.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Enter name of list")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            builder.setView(dialogLayout)

            // Using the dialogInterface to retrieve the input text and further call to the
            // function addTodo with the properties inputText and mutableListof
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
        checkTheme()
        btn_change_theme.setOnClickListener { chooseThemeDialog() }
    }

    private fun addTodo(item: Todo) {

        // Checks if the title is empty, if it is don´t call addTodo
        // Had some bugs with tmp and isEmpty() so decided to use .length > 0
        if (item.title.length > 0) {
            ListDepositoryManager.instance.addTodo(item)
        } else {
            Toast.makeText(applicationContext, "Title can't be blank!", Toast.LENGTH_LONG).show()
        }
    }

    private fun onListClicked(todo: Todo): Unit {
        ListHolder.PickedTodo = todo
        Log.e(TAG, "Pushed card : >${todo.toString()}")
        val intent = Intent(this, DetailsActivity::class.java)

        // Starting an activity with the intent of the currently clicked todo
        startActivity(intent)
    }

    private fun chooseThemeDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_theme_text))
        val styles = arrayOf("Light", "Dark", "System default")
        val checkedItem = MyPreferences(this).darkMode

        // Dialog pop up with 3 different choices
        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

            // Using the built in android studio .xml night theme, with manually set colors
            when (which) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    MyPreferences(this).darkMode = 0
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    MyPreferences(this).darkMode = 1
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    MyPreferences(this).darkMode = 2
                    delegate.applyDayNight()
                    dialog.dismiss()
                }

            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun checkTheme() {
        when (MyPreferences(this).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                delegate.applyDayNight()
            }
        }
    }
}

class MyPreferences(context: Context?) {

    companion object {
        private const val DARK_STATUS = "io.github.manuelernesto.DARK_STATUS"
    }

    /*
    Tried to use the default shared preference to track the phone default value, could not test
    this because i did not find any dark mode option on my emulator. I did install the gradle import but its still acting weird.
    */
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var darkMode = preferences.getInt(DARK_STATUS, 0)
        set(value) = preferences.edit().putInt(DARK_STATUS, value).apply()
}


package com.ikt205.inventory.data

import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.ikt205.inventory.adapter.DetailRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database

class ListDepositoryManager {

    private var URL: String = "https://inventory-b004f-default-rtdb.europe-west1.firebasedatabase.app/"
    private lateinit var listTodo: MutableList<Todo>
    val database = Firebase.database(URL)
    val myPath = database.getReference("") // The plan here was to add user functionality with a reference to each user
    var onList: ((List<Todo>) -> Unit)? = null

    fun load(url: String, context: Context) {
        URL = url
        listTodo = mutableListOf()
        initialReadFromDatabase()
        onList?.invoke(listTodo)
    }

    fun addItem(todo: Todo, itemList: Todo.Item) {
        val currentItemIndex = listTodo.indexOf(todo)
        listTodo[currentItemIndex].itemList.add(itemList)
        myPath.child("Todo").child(todo.title).child("itemList")
            .child(itemList.itemName).setValue(itemList)

        DetailRecyclerAdapter(todo.itemList, todo.title, this::reloadProgressBar).updateCollection(
            listTodo[currentItemIndex].itemList
        )
        onList?.invoke(listTodo)
        setPathValue()
    }

    fun addTodo(todo: Todo) {
        myPath.child("Todo").child(todo.title).setValue(todo)
        listTodo.add(todo)
        onList?.invoke(listTodo)
    }

    fun deleteItem(listKey: String, itemKey: String) {
        //Deleting entry from database not in the local mutable list
        myPath.child("Todo").child(listKey).child("itemList")
            .child(itemKey).removeValue()

        setPathValue()
    }

    fun deleteTodo(index: Int) {
        //Deleting entry from database
        myPath.child("Todo").child(listTodo[index].title).removeValue()

        //Deleting entry from local mutableList
        listTodo.removeAt(index)

        setPathValue()
    }

    fun flipItemStatus(listKey: String, item: Todo.Item, status: Boolean) {
        //Flipping status locally in mutableList
        item.flipStatus()

        //Flipping status entry on remote database
        myPath.child("Todo").child(listKey).child("itemList")
            .child(item.itemName).child("completed").setValue(!status)

        setPathValue()
    }

    fun reloadProgressBar() {
        onList?.invoke(listTodo)
    }

    fun setPathValue() {
        for (v in listTodo) {
            // Updating the path starting with Todo/title/(size or completed)
            myPath.child("Todo").child(v.title).child("size").setValue(v.getSize())
            myPath.child("Todo").child(v.title).child("completed").setValue(v.getCompleted())
        }
    }

    fun initialReadFromDatabase() {
        //Reads/updates the value from from database into local mutableLIst at startup
        myPath.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val snapshotIterator = snapshot.children.iterator()
                // Returns true if the if the iteration has more elements
                if (snapshotIterator.hasNext()) {
                    val itemIndex = snapshotIterator.next()
                    val itemsIterator = itemIndex.children.iterator()

                    // Reads from the database and adds the data to a temporary list
                    while (itemsIterator.hasNext()) {
                        //get current item
                        val itemHashMap: HashMap<String, Any>
                        val temporaryList = mutableListOf<Todo.Item>()
                        val databaseItem = itemsIterator.next()

                        if (databaseItem.child("itemList").getValue() != null) {
                            itemHashMap = databaseItem.child("itemList").getValue() as HashMap<String, Any>

                            for ((k, v) in itemHashMap) {
                                v as HashMap<String, Boolean>
                                temporaryList.add(
                                    Todo.Item(
                                        v.get("itemName").toString(),
                                        v.get("completed")!!
                                    )
                                )
                            }
                        }
                        // Adds the temporary list to the main list
                        listTodo!!.add(Todo(databaseItem.key.toString(), temporaryList))
                    }
                }

                onList?.invoke(listTodo)
            }
            // Returns database error on transaction cancellation.
            override fun onCancelled(error: DatabaseError) {
                Log.w("DatabaseError", error.details)
            }
        }
        )
    }

    companion object {
        val instance = ListDepositoryManager()
    }
}


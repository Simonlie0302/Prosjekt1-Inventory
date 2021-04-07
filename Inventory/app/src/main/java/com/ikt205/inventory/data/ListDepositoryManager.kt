package com.ikt205.inventory.data

import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.ikt205.inventory.adapter.DetailRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database


private var URL: String = "https://inventory-b004f-default-rtdb.europe-west1.firebasedatabase.app/"

class ListDepositoryManager {

    private lateinit var listCollection: MutableList<Todo>
    val database = Firebase.database(URL)
    val myPath = database.getReference("")
    var onList: ((List<Todo>) -> Unit)? = null
    var onListUpdate: ((todo: Todo) -> Unit)? = null


    fun load(url: String, context: Context) {

        URL = url
        listCollection = mutableListOf()
        readFromRealtimeDatabase()
        onList?.invoke(listCollection)
    }

    fun updateItem(todo: Todo) {
        // finn bok i listen og erstat med den nye boken.
        onListUpdate?.invoke(todo)
    }

    fun addTodo(todo: Todo) {
        myPath.child("Todo").child(todo.title).setValue(todo)
        listCollection.add(todo)
        onList?.invoke(listCollection)
    }

    fun reloadProgressBar() {
        onList?.invoke(listCollection)
    }

    fun addItem(todo: Todo, itemList: Todo.Item) {
        val currentItemIndex = listCollection.indexOf(todo)
        listCollection[currentItemIndex].itemList.add(itemList)
        myPath.child("Todo").child(todo.title).child("itemList")
            .child(itemList.itemName).setValue(itemList)

        DetailRecyclerAdapter(todo.itemList, todo.title, this::reloadProgressBar).updateCollection(listCollection[currentItemIndex].itemList)
        onList?.invoke(listCollection)
        updateStats()
    }

    fun deleteItem(listKey: String, itemKey: String) {
        //Deleting entry from database. Deletion from local mutableList is done in adapter
        myPath.child("Todo").child(listKey).child("itemList")
            .child(itemKey).removeValue()

        updateStats()
    }

    fun deleteTodo(index: Int) {
        //Deleting entry from database
        myPath.child("Todo").child(listCollection[index].title).removeValue()

        //Deleting entry from local mutableList
        listCollection.removeAt(index)

        updateStats()
    }

    fun flipStatus(listKey: String, item: Todo.Item, status: Boolean) {
        //Flipping status locally in mutableList
        item.flipStatus()

        //Flipping status entry on remote database
        myPath.child("Todo").child(listKey).child("itemList")
            .child(item.itemName).child("completed").setValue(!status)

        updateStats()
    }

    fun updateStats(){
        for (v in listCollection) {
            myPath.child("Todo").child(v.title).child("size").setValue(v.getSize())
            myPath.child("Todo").child(v.title).child("completed").setValue(v.getCompleted())
        }
    }

    fun readFromRealtimeDatabase() {
        //Complex interface between app and firebase databse - Reads/updates the value from from database into local mutableLIst at startup
        myPath.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dBitems = snapshot.children.iterator()
                if (dBitems.hasNext()) {
                    val toDoListindex = dBitems.next()
                    val itemsIterator = toDoListindex.children.iterator()
                    while (itemsIterator.hasNext()) {
                        //get current item
                        val currentItem = itemsIterator.next()
                        val itemMap: HashMap<String, Any>
                        val tmp = mutableListOf<Todo.Item>()
                        if (currentItem.child("itemList").getValue() != null) {
                            itemMap =
                                currentItem.child("itemList").getValue() as HashMap<String, Any>
                            for ((k, v) in itemMap) {
                                v as HashMap<String, Boolean>
                                tmp.add(
                                    Todo.Item(
                                        v.get("itemName").toString(),
                                        v.get("completed")!!
                                    )
                                )
                            }
                        }
                        listCollection!!.add(Todo(currentItem.key.toString(), tmp))
                    }
                }
                onList?.invoke(listCollection)
            }
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


package com.ikt205.inventory.data

import android.content.Context
import com.ikt205.inventory.DetailRecyclerAdapter
import com.ikt205.inventory.DetailsActivity

class ListDepositoryManager {

    private lateinit var listCollection: MutableList<Todo>
    private lateinit var itemList: MutableList<Todo.Item>


    var onList: ((List<Todo>) -> Unit)? = null
    var onListUpdate: ((todo: Todo) -> Unit)? = null
    var itemListe: ((List<Todo.Item>) -> Unit)? = null


    fun load(url: String, context: Context) {

        if (!this::listCollection.isInitialized){
        listCollection = mutableListOf()
        }
//        if (!this::itemList.isInitialized){
//            itemList = mutableListOf()
//        }
        // onList?.invoke(listCollection) // Update the collection


        /*  queue = Volley.newRequestQueue(context)
          val request = JsonArrayRequest(Request.Method.GET, url, null,
              {
                  // JSON -> transport formatet
                  // Gson -> Manipulering og serialisering av json
                  Log.d("BookDepositoryManager", it.toString(3))
              },
              {
                  Log.e("BookDepositoryManager", it.toString())
              })
          queue.add(request)*/


        listCollection = mutableListOf(
            Todo(
                "Must watch Movies", mutableListOf(
                    Todo.Item("Interstellar", true),
                    Todo.Item("Joker", false),
                    Todo.Item("The boys", true)
                )
            ),
            Todo(
                "School work", mutableListOf(
                    Todo.Item("Ikt205 prosjekt", true),
                    Todo.Item("Data kommunikasjon", false),
                    Todo.Item("Statistikk", false)
                ),
            )
        )
        onList?.invoke(listCollection)
    }

    fun updateItem(todo: Todo) {
        // finn bok i listen og erstat med den nye boken.
        onListUpdate?.invoke(todo)
    }

    fun addTodo(todo: Todo) {
        println(todo)
        listCollection.add(todo)
        onList?.invoke(listCollection)
    }

    fun reloadProgressBar(){
        onList?.invoke(listCollection)
    }

    fun addItem(todo: Todo, itemList: Todo.Item) {
        val currentItemIndex = listCollection.indexOf(todo)
        listCollection[currentItemIndex].itemList.add(itemList)
        DetailRecyclerAdapter(todo.itemList).updateCollection(listCollection[currentItemIndex].itemList)
        onList?.invoke(listCollection)
    }

    fun deleteTodo(index: Int) {
        listCollection.removeAt(index)
    }

    companion object {
        val instance = ListDepositoryManager()
    }
}

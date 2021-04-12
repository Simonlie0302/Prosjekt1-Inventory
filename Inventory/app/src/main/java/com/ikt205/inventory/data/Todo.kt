package com.ikt205.inventory.data

import java.io.Serializable

data class Todo(
    var title: String,
    val itemList: MutableList<Item>
) : Serializable {

    data class Item(var itemName: String, var completed: Boolean) {

        fun flipStatus() {
            completed = !completed
        }
    }

    fun getSize(): Int {
        return itemList.size
    }

    fun getCompleted(): Int {
        var finished: Int = 0
        for (i in itemList) {
            print(i.completed)
            if (i.completed == true)
                finished += 1
        }
        return finished
    }
}
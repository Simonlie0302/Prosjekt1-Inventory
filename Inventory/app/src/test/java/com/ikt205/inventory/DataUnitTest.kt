package com.ikt205.inventory

import com.ikt205.inventory.data.Todo
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class UnitTest {

    @Test
    fun dataIsCorrectFormat(){
        assertEquals("Todo(title=Must watch movies, " +
                "itemList=[Item(itemName=Interstellar, completed=true), " +
                "Item(itemName=Shutter Island, completed=true), " +
                "Item(itemName=Inception, completed=false)])",

                Todo("Must watch movies",
                mutableListOf(Todo.Item("Interstellar", true),
                    Todo.Item("Shutter Island", true),
                    Todo.Item("Inception", false))).toString())
    }

    @Test
    fun numberOfCompletedIsCorrect(){
        assertEquals(2,
            Todo("Must watch movies",
                mutableListOf(Todo.Item("Catch me if you can", true),
                    Todo.Item("Shutter Island", true),
                    Todo.Item("The Wolf of Wall Street", false))).getCompleted())
    }

    @Test
    fun sizeIsCorrect(){
        assertEquals(4, Todo("Must watch movies",
                mutableListOf(Todo.Item("Catch me if you can", true),
                    Todo.Item("Shutter Island", true),
                    Todo.Item("The Departed", false),
                    Todo.Item("The Wolf of Wall Street", true))).getSize())
    }
}
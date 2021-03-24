package com.ikt205.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikt205.inventory.databinding.FragmentHomePageBinding
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlin.random.Random

/**
 * A simple fragment subclass as the main(default) destination in the navigation.
 * TODO: Change name of the activities to something more suitable
 * TODO: Add some commenting
 */
class HomePage : Fragment(), RecyclerAdapter.OnItemClickListener {

    private val testList = generateDummyList(10)
    private val adapter = RecyclerAdapter(testList, this)
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomePageBinding.inflate(inflater, container, false)

        return binding.root
    }

    // Generating a dummy list for testing purposes
    private fun generateDummyList(size: Int): ArrayList<RecyclerViewItem> {
        val list = ArrayList<RecyclerViewItem>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_baseline_6_ft
                1 -> R.drawable.ic_baseline_accessible
                else -> R.drawable.ic_baseline_5g
            }
            val item = RecyclerViewItem(drawable, "Item $i", "Line 2")
            list += item
        }
        return list
    }

    override fun onItemClick(position: Int) {
        val index = position

        testList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
        // Change linearLayoutManager to make a grid system
        recycler_view.layoutManager = LinearLayoutManager(this.context)
        recycler_view.setHasFixedSize(true)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.scrum_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scrum_section.adapter.TaskAdapter
import com.example.scrum_section.data.TaskRepository
import com.example.scrum_section.util.TaskStatus
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScrumFragment : Fragment() {
    private lateinit var adapter: TaskAdapter
    private var currentStatus = TaskStatus.ALL
    private var fullTaskList = listOf<com.example.scrum_section.model.Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scrum, container, false)

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvTasks)
        rv.layoutManager = LinearLayoutManager(requireContext())
        fullTaskList = TaskRepository.getTasksByStatus(currentStatus)
        adapter = TaskAdapter(fullTaskList)
        rv.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.btnAddTask)
        fab.setOnClickListener {
            AddTaskDialog {
                refreshData()
            }.show(parentFragmentManager, "AddTaskDialog")
        }

        // 🔹 Tab status
        view.findViewById<View>(R.id.btnAll).setOnClickListener { currentStatus = TaskStatus.ALL; refreshData() }
        view.findViewById<View>(R.id.btnToDo).setOnClickListener { currentStatus = TaskStatus.TODO; refreshData() }
        view.findViewById<View>(R.id.btnInProgress).setOnClickListener { currentStatus = TaskStatus.IN_PROGRESS; refreshData() }
        view.findViewById<View>(R.id.btnToVerify).setOnClickListener { currentStatus = TaskStatus.TO_VERIFY; refreshData() }
        view.findViewById<View>(R.id.btnDone).setOnClickListener { currentStatus = TaskStatus.DONE; refreshData() }

        // 🔹 SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndSort(newText)
                return true
            }
        })

        // 🔹 Spinner Sort
        val spinnerSort = view.findViewById<Spinner>(R.id.spinnerSort)
        val sortOptions = arrayOf("Terbaru", "Terlama", "A-Z", "Z-A")
        spinnerSort.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, itemId: Long) {
                filterAndSort(searchView.query.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        return view
    }

    private fun refreshData() {
        fullTaskList = TaskRepository.getTasksByStatus(currentStatus)
        filterAndSort("")
    }

    private fun filterAndSort(query: String?) {
        val searchText = query?.lowercase()?.trim() ?: ""
        val filtered = fullTaskList.filter { it.name.lowercase().contains(searchText) }

        val spinnerSort = view?.findViewById<Spinner>(R.id.spinnerSort)
        val sortType = spinnerSort?.selectedItem?.toString() ?: "Terbaru"

        val sorted = when (sortType) {
            "A-Z" -> filtered.sortedBy { it.name.lowercase() }
            "Z-A" -> filtered.sortedByDescending { it.name.lowercase() }
            "Terlama" -> filtered.sortedBy { it.id }
            else -> filtered.sortedByDescending { it.id } // default Terbaru
        }

        adapter.updateData(sorted)
    }
}

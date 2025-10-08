package com.example.scrum_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

        // 🔸 Tombol status
        val btnAll = view.findViewById<Button>(R.id.btnAll)
        val btnToDo = view.findViewById<Button>(R.id.btnToDo)
        val btnInProgress = view.findViewById<Button>(R.id.btnInProgress)
        val btnToVerify = view.findViewById<Button>(R.id.btnToVerify)
        val btnDone = view.findViewById<Button>(R.id.btnDone)

        val buttons = listOf(
            btnAll to TaskStatus.ALL,
            btnToDo to TaskStatus.TODO,
            btnInProgress to TaskStatus.IN_PROGRESS,
            btnToVerify to TaskStatus.TO_VERIFY,
            btnDone to TaskStatus.DONE
        )

        fun selectButton(selected: TaskStatus) {
            buttons.forEach { (btn, status) ->
                val color = if (status == selected)
                    requireContext().getColor(R.color.redw)
                else when (status) {
                    TaskStatus.TODO -> requireContext().getColor(R.color.blue)
                    TaskStatus.IN_PROGRESS -> requireContext().getColor(R.color.orange)
                    TaskStatus.TO_VERIFY -> requireContext().getColor(R.color.purple)
                    TaskStatus.DONE -> requireContext().getColor(R.color.green)
                    else -> requireContext().getColor(R.color.gray)
                }
                btn.backgroundTintList = android.content.res.ColorStateList.valueOf(color)
            }
        }

        buttons.forEach { (btn, status) ->
            btn.setOnClickListener {
                currentStatus = status
                refreshData()
                selectButton(status)
            }
        }

        // 🔍 SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndSort(newText)
                return true
            }
        })

        // 🔽 Spinner Sort
        val spinnerSort = view.findViewById<Spinner>(R.id.spinnerSort)
        val sortOptions = arrayOf("Terbaru", "Terlama", "A-Z", "Z-A")
        spinnerSort.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                filterAndSort(searchView.query.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        selectButton(TaskStatus.ALL)
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

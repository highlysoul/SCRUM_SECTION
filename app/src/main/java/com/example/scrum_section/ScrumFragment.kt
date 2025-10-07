package com.example.scrum_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scrum_section.R
import com.example.scrum_section.adapter.TaskAdapter
import com.example.scrum_section.data.TaskRepository
import com.example.scrum_section.util.TaskStatus
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScrumFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private var currentStatus = TaskStatus.TODO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scrum, container, false)

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvTasks)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = TaskAdapter(TaskRepository.getTasksByStatus(currentStatus))
        rv.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.btnAddTask)
        fab.setOnClickListener {
            AddTaskDialog {
                refreshData()
            }.show(parentFragmentManager, "AddTaskDialog")
        }

        // Tab buttons
        view.findViewById<View>(R.id.btnToDo).setOnClickListener {
            currentStatus = TaskStatus.TODO
            refreshData()
        }
        view.findViewById<View>(R.id.btnInProgress).setOnClickListener {
            currentStatus = TaskStatus.IN_PROGRESS
            refreshData()
        }
        view.findViewById<View>(R.id.btnToVerify).setOnClickListener {
            currentStatus = TaskStatus.TO_VERIFY
            refreshData()
        }
        view.findViewById<View>(R.id.btnDone).setOnClickListener {
            currentStatus = TaskStatus.DONE
            refreshData()
        }

        return view
    }

    private fun refreshData() {
        adapter.updateData(TaskRepository.getTasksByStatus(currentStatus))
    }
}
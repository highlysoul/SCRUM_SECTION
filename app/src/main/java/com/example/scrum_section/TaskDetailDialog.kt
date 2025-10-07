package com.example.scrum_section

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.scrum_section.model.Task
import com.example.scrum_section.R

class TaskDetailDialog(private val task: Task) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_task_detail)

        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val tvDeadline = dialog.findViewById<TextView>(R.id.tvDeadline)
        val tvDepartment = dialog.findViewById<TextView>(R.id.tvDepartment)
        val tvDescription = dialog.findViewById<TextView>(R.id.tvDescription)
        val btnClose = dialog.findViewById<TextView>(R.id.btnClose)

        tvName.text = task.name
        tvDeadline.text = task.deadline
        tvDepartment.text = task.department
        tvDescription.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."

        btnClose.setOnClickListener { dismiss() }

        return dialog
    }
}

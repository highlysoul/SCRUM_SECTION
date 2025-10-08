package com.example.scrum_section

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.scrum_section.data.TaskRepository
import com.example.scrum_section.model.Task
import com.example.scrum_section.util.TaskStatus

class EditTaskDialog(private val task: Task, private val onUpdated: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_task)

        val etName = dialog.findViewById<EditText>(R.id.etName)
        val etDeadline = dialog.findViewById<EditText>(R.id.etDeadline)
        val etDepartment = dialog.findViewById<EditText>(R.id.etDepartment)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAdd)

        etName.setText(task.name)
        etDeadline.setText(task.deadline)
        etDepartment.setText(task.department)
        btnAdd.text = "Update"

        btnAdd.setOnClickListener {
            task.name = etName.text.toString()
            task.deadline = etDeadline.text.toString()
            task.department = etDepartment.text.toString()

            // 🔹 Update ke repository
            TaskRepository.updateTask(task)

            onUpdated()
            dismiss()
        }

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }
}

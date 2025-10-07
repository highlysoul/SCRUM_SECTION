package com.example.scrum_section.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.scrum_section.EditTaskDialog
import com.example.scrum_section.R
import com.example.scrum_section.model.Task

class TaskAdapter(private var tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvTaskName)
        val tvCreatedBy: TextView = itemView.findViewById(R.id.tvCreatedBy)
        val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val btnChangeStatus: ImageView = itemView.findViewById(R.id.btnChangeStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        // --- isi data ke view ---
        holder.tvName.text = task.name
        holder.tvCreatedBy.text = "by ${task.createdBy}"
        holder.tvDeadline.text = task.deadline
        holder.tvStatus.text = task.status.name.replace("_", " ")

        // --- style warna status biar mirip Tratify ---
        val colorRes = when (task.status.name) {
            "TODO" -> R.color.blue
            "IN_PROGRESS" -> R.color.orange
            "TO_VERIFY" -> R.color.purple
            "DONE" -> R.color.green
            else -> R.color.gray
        }
        holder.tvStatus.setBackgroundResource(colorRes)

        // --- tombol edit task ---
        holder.btnChangeStatus.setOnClickListener {
            val activity = holder.itemView.context as FragmentActivity
            EditTaskDialog(task) {
                notifyDataSetChanged()
            }.show(activity.supportFragmentManager, "EditTaskDialog")
        }
    }

    fun updateData(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}

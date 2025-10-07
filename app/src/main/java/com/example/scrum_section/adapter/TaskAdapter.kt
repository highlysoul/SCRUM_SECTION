package com.example.scrum_section.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.scrum_section.R
import com.example.scrum_section.model.Task
import com.example.scrum_section.util.TaskStatus
import com.example.scrum_section.data.TaskRepository

class TaskAdapter(private var tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvTaskName)
        val tvCreatedBy: TextView = itemView.findViewById(R.id.tvCreatedBy)
        val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val btnChangeStatus: ImageView = itemView.findViewById(R.id.btnChangeStatus)
        val spinnerStatus: Spinner = itemView.findViewById(R.id.spinnerStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.tvName.text = task.name
        holder.tvCreatedBy.text = "by ${task.createdBy}"
        holder.tvDeadline.text = task.deadline
        holder.tvStatus.text = task.status.name.replace("_", " ")

        val colorRes = when (task.status.name) {
            "TODO" -> R.color.blue
            "IN_PROGRESS" -> R.color.orange
            "TO_VERIFY" -> R.color.purple
            "DONE" -> R.color.green
            else -> R.color.gray
        }
        holder.tvStatus.setBackgroundResource(colorRes)

        // 🟢 dropdown ganti status
        val statusList = TaskStatus.values().map { it.name }
        val spinnerAdapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_dropdown_item,
            statusList
        )
        holder.spinnerStatus.adapter = spinnerAdapter
        holder.spinnerStatus.setSelection(statusList.indexOf(task.status.name))
        holder.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, pos: Int, id: Long
            ) {
                val newStatus = TaskStatus.valueOf(statusList[pos])
                if (task.status != newStatus) {
                    task.status = newStatus
                    TaskRepository.updateTask(task)
                    notifyItemChanged(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 🟢 klik tombol edit tetap jalan
        holder.btnChangeStatus.setOnClickListener {
            holder.spinnerStatus.performClick()
        }
    }

    fun updateData(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}

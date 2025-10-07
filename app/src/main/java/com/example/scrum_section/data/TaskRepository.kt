package com.example.scrum_section.data

import com.example.scrum_section.model.Task
import com.example.scrum_section.util.TaskStatus

object TaskRepository {
    private val tasks = mutableListOf(
        Task(1, "Design UI Login", TaskStatus.TODO),
        Task(2, "Implement RecyclerView", TaskStatus.IN_PROGRESS),
        Task(3, "Fix Adapter Binding", TaskStatus.DONE)
    )

    fun getTasks(): List<Task> = tasks

    fun getTasksByStatus(status: TaskStatus): List<Task> {
        return tasks.filter { it.status == status }
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun updateTaskStatus(id: Int, newStatus: TaskStatus) {
        tasks.find { it.id == id }?.status = newStatus
    }
}

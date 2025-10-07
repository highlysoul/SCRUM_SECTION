package com.example.scrum_section.data

import com.example.scrum_section.model.Task
import com.example.scrum_section.util.TaskStatus

object TaskRepository {
    private val tasks = mutableListOf<Task>()

    fun getTasks(): List<Task> = tasks

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun updateTask(index: Int, updatedTask: Task) {
        tasks[index] = updatedTask
    }

    fun getTasksByStatus(status: TaskStatus): List<Task> {
        return if (status == TaskStatus.ALL) {
            tasks
        } else {
            tasks.filter { it.status == status }
        }
    }

    // Contoh data awal
    init {
        tasks.add(Task(1, "Design UI", "Reza", "2025-10-07", "Frontend", TaskStatus.TODO))
        tasks.add(Task(2, "Setup Database", "Hansen", "2025-10-10", "Backend", TaskStatus.IN_PROGRESS))
        tasks.add(Task(3, "Integrate API", "Reza", "2025-10-12", "Backend", TaskStatus.TO_VERIFY))
        tasks.add(Task(4, "Testing", "Hansen", "2025-10-15", "QA", TaskStatus.DONE))
    }
}

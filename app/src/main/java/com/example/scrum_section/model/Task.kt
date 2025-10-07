package com.example.scrum_section.model

import com.example.scrum_section.util.TaskStatus

data class Task(
    val id: Int,
    val name: String,
    var status: TaskStatus
)

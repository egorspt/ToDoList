package com.app.todolist.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    val text: String,
    val dateCompletion: Long,
    val dateCreating: Long,
    var isDone: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
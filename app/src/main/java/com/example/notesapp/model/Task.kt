package com.example.notesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val taskTitle : String,
    val taskDesc : String,
    val dueDate: String,
    val completed: Boolean,
    val completedDate: String?
): Parcelable{
    fun markAsCompleted(completedDate: String): Task {
        return Task(id, taskTitle, taskDesc, dueDate, true, completedDate)
    }
}



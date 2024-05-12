package com.example.notesapp.repository

import com.example.notesapp.database.TaskDatabase
import com.example.notesapp.model.Task

class TaskRepository(private val db: TaskDatabase) {

    suspend fun insertTask(task: Task) = db.getTaskDao().insertTask(task)
    suspend fun deleteTask(task: Task) = db.getTaskDao().deleteTask(task)
    suspend fun updateTask(task: Task) = db.getTaskDao().updateTask(task)

    fun getAllTasks() = db.getTaskDao().getAllTasks()
    fun searchTask(query: String?) = db.getTaskDao().searchTask(query)
    fun getNonCompletedTasks() = db.getTaskDao().getNonCompletedTasks()
    fun getCompletedTasks() = db.getTaskDao().getCompletedTasks()
    fun searchCompletedTask(query: String?) = db.getTaskDao().searchCompletedTask(query)
}

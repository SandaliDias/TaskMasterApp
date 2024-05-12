package com.example.notesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.Task
import com.example.notesapp.repository.TaskRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskViewModel(app: Application, private val taskRepository: TaskRepository): AndroidViewModel(app) {

    fun addTask(task: Task) =
        viewModelScope.launch{
            taskRepository.insertTask(task)
        }

    fun deleteTask(task: Task) =
        viewModelScope.launch{
            taskRepository.deleteTask(task)
        }

    fun updateTask(task: Task) =
        viewModelScope.launch{
            taskRepository.updateTask(task)
        }

    fun getAllTasks() = taskRepository.getAllTasks()

    fun getNonCompletedTasks() = taskRepository.getNonCompletedTasks()

    fun getCompletedTasks() = taskRepository.getCompletedTasks()

    fun searchTask(query: String?) =
        taskRepository.searchTask(query)

    fun markTaskAsCompleted(task: Task) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val completedTask = task.markAsCompleted(currentDate)
        updateTask(completedTask)
    }

    fun searchCompletedTask(query: String?) =
        taskRepository.searchCompletedTask(query)
}
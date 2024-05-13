package com.example.notesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    fun getAllTasks(): LiveData<List<Task>> =
        taskRepository.getAllTasks()

    fun getNonCompletedTasks(): LiveData<List<Task>> =
        taskRepository.getNonCompletedTasks()

    fun getCompletedTasks(): LiveData<List<Task>> =
        taskRepository.getCompletedTasks()

    fun searchTask(query: String?): LiveData<List<Task>> =
        taskRepository.searchTask(query)

    fun markTaskAsCompleted(task: Task) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val completedTask = task.markAsCompleted(currentDate)
        updateTask(completedTask)
    }

    fun searchCompletedTask(query: String?): LiveData<List<Task>> =
        taskRepository.searchCompletedTask(query)
}

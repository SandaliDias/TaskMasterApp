package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesapp.model.Task


@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY id DESC")
    fun getNonCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE completed = 1 ORDER BY id DESC")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS ORDER BY id DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS WHERE taskTitle LIKE :query and completed = 0")
    fun searchTask(query: String?): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS WHERE taskTitle LIKE :query and completed = 1")
    fun searchCompletedTask(query: String?): LiveData<List<Task>>
}
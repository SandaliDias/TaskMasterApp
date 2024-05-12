package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.CompleteTaskLayoutBinding
import com.example.notesapp.model.Task
import com.example.notesapp.viewmodel.TaskViewModel

class CompleteTaskAdapter(private val taskViewModel: TaskViewModel) : RecyclerView.Adapter<CompleteTaskAdapter.CompleteTaskViewHolder>() {

    private var tasks: List<Task> = emptyList()

    inner class CompleteTaskViewHolder(val itemBinding: CompleteTaskLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(task: Task) {
            itemBinding.taskTitle.text = task.taskTitle
            itemBinding.taskDesc.text = task.taskDesc
            itemBinding.dueDate.text = "Due Date:" + task.dueDate
            itemBinding.completedDate.text = "Completed Date:" + task.completedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompleteTaskViewHolder {
        val itemBinding = CompleteTaskLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CompleteTaskViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CompleteTaskViewHolder, position: Int) {
        holder.bind(tasks[position])
        val currentTask = tasks[position]

        // Handle "done" button click
        holder.itemBinding.btnDelete.setOnClickListener {
            taskViewModel.deleteTask(currentTask)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun submitList(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}

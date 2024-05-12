package com.example.notesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.databinding.TaskLayoutBinding
import com.example.notesapp.fragment.HomeFragmentDirections
import com.example.notesapp.model.Task
import com.example.notesapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val context: Context,
    private val taskViewModel: TaskViewModel // Add TaskViewModel parameter
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<Task> = emptyList()

    class TaskViewHolder(val itemBinding: TaskLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemBinding =
            TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]

        // Set task data
        holder.itemBinding.taskTitle.text = currentTask.taskTitle
        holder.itemBinding.taskDesc.text = currentTask.taskDesc
        holder.itemBinding.dueDate.text = "Due Date:" + currentTask.dueDate

        // Parse due date
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dueDate = sdf.parse(currentTask.dueDate)
        val currentDate = Date()

        // Determine background drawable based on due date proximity
        val backgroundDrawable = if (dueDate != null && currentDate != null) {
            val diffInMillies = dueDate.time - currentDate.time
            val diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)).toInt()

            when {
                diffInDays <= 1 -> R.drawable.due_date_border
                else -> R.drawable.border
            }
        } else {
            R.drawable.border
        }

        // Set background drawable
        ContextCompat.getDrawable(context, backgroundDrawable)?.let {
            holder.itemBinding.root.background = it
        }

        // Determine text colors based on due date proximity
        val textColors = if (dueDate != null && currentDate != null) {
            val diffInMillies = dueDate.time - currentDate.time
            val diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)).toInt()

            when {
                diffInDays <= 1 -> ContextCompat.getColor(context, android.R.color.white)
                else -> ContextCompat.getColor(context, android.R.color.black)
            }
        } else {
            ContextCompat.getColor(context, android.R.color.black)
        }

        // Set text colors
        holder.itemBinding.taskTitle.setTextColor(textColors)
        holder.itemBinding.taskDesc.setTextColor(textColors)
        holder.itemBinding.dueDate.setTextColor(textColors)

        // Handle item click
        holder.itemView.setOnClickListener {
            val direction =
                HomeFragmentDirections.actionHomeFragmentToEditTaskFragment(currentTask)
            it.findNavController().navigate(direction)
        }

        // Handle "done" button click
        holder.itemBinding.btnDone.setOnClickListener {
            taskViewModel.markTaskAsCompleted(currentTask)
        }
    }

    fun submitList(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}

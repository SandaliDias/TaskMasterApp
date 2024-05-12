package com.example.notesapp.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentAddTaskBinding
import com.example.notesapp.model.Task
import com.example.notesapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddTaskFragment : Fragment(R.layout.fragment_add_task), MenuProvider {

    private var addTaskBinding: FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addTaskBinding = FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view

        binding.addTaskDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun saveTask(view: View) {
        val taskTitle = binding.addTaskTitle.text.toString().trim()
        val taskDesc = binding.addTaskDesc.text.toString().trim()
        val dueDate = binding.addTaskDate.text.toString().trim()

        if (taskTitle.isNotEmpty()) {
            val calendar = Calendar.getInstance()
            val currentDate = calendar.time

            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dueDate)

            if (selectedDate != null && selectedDate.before(currentDate)) {
                Toast.makeText(addTaskView.context, "Due date cannot be before the current date", Toast.LENGTH_SHORT).show()
            } else {
                val task = Task(0, taskTitle, taskDesc, dueDate, false, null)
                taskViewModel.addTask(task)
                Toast.makeText(addTaskView.context, "Task Saved", Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
        } else {
            Toast.makeText(addTaskView.context, "Please enter task title", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            binding.addTaskDate.setText(selectedDate)
        }, year, month, day)
        datePickerDialog.show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveTask(addTaskView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }
}

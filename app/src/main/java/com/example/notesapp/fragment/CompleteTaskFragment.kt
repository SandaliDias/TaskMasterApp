package com.example.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.CompleteTaskAdapter
import com.example.notesapp.databinding.FragmentCompleteTaskBinding
import com.example.notesapp.model.Task
import com.example.notesapp.viewmodel.TaskViewModel

class CompleteTaskFragment : Fragment() {

    private var completeTaskBinding: FragmentCompleteTaskBinding? = null
    private val binding get() = completeTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var completeTaskAdapter: CompleteTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        completeTaskBinding = FragmentCompleteTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = (activity as MainActivity).taskViewModel
        setupCompleteTasksRecyclerView()

        taskViewModel.getCompletedTasks().observe(viewLifecycleOwner, Observer { tasks ->
            completeTaskAdapter.submitList(tasks)
            updateUI(tasks)
        })

        binding.ongoingTasksButton.setOnClickListener{
            it.findNavController().navigate(R.id.action_completeTaskFragment_to_homeFragment)
        }
    }

    private fun setupCompleteTasksRecyclerView() {
        completeTaskAdapter = CompleteTaskAdapter(taskViewModel)
        binding.completeTasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = completeTaskAdapter
        }
    }

    private fun updateUI(tasks: List<Task>) {
        if (tasks.isNotEmpty()) {
            binding.emptyCompleteTasksImage.visibility = View.GONE
            binding.completeTasksRecyclerView.visibility = View.VISIBLE
        } else {
            binding.emptyCompleteTasksImage.visibility = View.VISIBLE
            binding.completeTasksRecyclerView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        completeTaskBinding = null
    }
}

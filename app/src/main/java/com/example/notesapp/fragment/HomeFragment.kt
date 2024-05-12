package com.example.notesapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.TaskAdapter
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.model.Task
import com.example.notesapp.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,
    MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        setupHomeRecyclerView(requireContext())

        binding.addTaskFab.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }

        binding.completedTasksButton.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_completeTaskFragment)
        }
    }

    private fun updateUI(tasks: List<Task>){
        if(tasks.isNotEmpty()){
            binding.emptyTasksImage.visibility = View.GONE
            binding.homeRecyclerView.visibility  = View.VISIBLE
        } else{
            binding.emptyTasksImage.visibility = View.VISIBLE
            binding.homeRecyclerView.visibility  = View.GONE
        }
    }

    private fun setupHomeRecyclerView(context: Context) {
        taskAdapter = TaskAdapter(context, taskViewModel) // Pass taskViewModel here
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = taskAdapter
        }

        taskViewModel.getNonCompletedTasks().observe(viewLifecycleOwner) { tasks ->
            val sortedTasks = tasks.sortedBy { task ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.parse(task.dueDate)
                date?.time ?: Long.MAX_VALUE
            }

            taskAdapter.submitList(sortedTasks)
            updateUI(sortedTasks)
        }
    }

    private fun searchTask(query: String){
        val searchQuery = "%$query%"

        taskViewModel.searchTask(searchQuery).observe(viewLifecycleOwner){list ->
            val sortedTasks = list.sortedBy { task ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.parse(task.dueDate)
                date?.time ?: Long.MAX_VALUE
            }

            taskAdapter.submitList(sortedTasks)
            updateUI(sortedTasks)
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchTask(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        searchTask(newText)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = true
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}

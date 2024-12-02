package com.example.studentmanager3

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.fragment.app.activityViewModels

class StudentListFragment : Fragment() {

    private lateinit var listView: ListView
    private val studentViewModel: StudentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Kích hoạt menu trong Fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        listView = view.findViewById(R.id.listViewStudents)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf<String>())
        listView.adapter = adapter

        studentViewModel.students.observe(viewLifecycleOwner) { students ->
            val studentNames = students.map { "${it.name} - ${it.mssv}" }
            adapter.clear()
            adapter.addAll(studentNames)
            adapter.notifyDataSetChanged()
        }

        // Register ListView for context menu
        registerForContextMenu(listView)
        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_student_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                findNavController().navigate(R.id.addEditStudentFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(0, 1, 0, "Edit")
        menu.add(0, 2, 1, "Remove")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        when (item.itemId) {
            1 -> { // Edit
                val selectedStudent = studentViewModel.students.value?.get(position)
                selectedStudent?.let {
                    val action = StudentListFragmentDirections
                        .actionToAddEditStudentFragment(it.name, it.mssv)
                    findNavController().navigate(action)
                }
            }
            2 -> { // Remove
                studentViewModel.removeStudent(position)
            }
        }
        return super.onContextItemSelected(item)
    }

    data class Student(val name: String, val mssv: String) {
    }
    class StudentViewModel : ViewModel() {
        private val _students = MutableLiveData<MutableList<Student>>(
            mutableListOf(
                Student("Nguyen Van A", "20215439"),
                Student("Tran Van B", "20215438")
            )
        )
        val students: LiveData<MutableList<Student>> get() = _students

        fun addStudent(student: Student) {
            _students.value?.add(student)
            _students.value = _students.value // Notify observers
        }

        fun removeStudent(position: Int) {
            _students.value?.removeAt(position)
            _students.value = _students.value // Notify observers
        }

        fun updateStudent(oldName: String, newName: String, newMssv: String) {
            _students.value?.let { list ->
                val index = list.indexOfFirst { it.name == oldName }
                if (index != -1) {
                    list[index] = Student(newName, newMssv)
                    _students.value = list // Notify observers
                }
            }
        }

    }
}

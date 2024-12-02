package com.example.studentmanager3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs

class AddEditStudentFragment : Fragment() {

    private lateinit var editTextName: EditText
    private lateinit var editTextMSSV: EditText
    private lateinit var buttonSave: Button

    private val studentViewModel: StudentListFragment.StudentViewModel by activityViewModels()
    private val args: AddEditStudentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_student, container, false)

        editTextName = view.findViewById(R.id.editTextName)
        editTextMSSV = view.findViewById(R.id.editTextMSSV)
        buttonSave = view.findViewById(R.id.buttonSave)

        // Load data for editing
        val editing = args.name != "" && args.mssv != ""
        if (editing) {
            editTextName.setText(args.name)
            editTextMSSV.setText(args.mssv)
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val mssv = editTextMSSV.text.toString()

            if (editing) {
                println("edit")
                studentViewModel.updateStudent(args.name!!, name, mssv)
            } else {
                println("add")
                studentViewModel.addStudent(StudentListFragment.Student(name, mssv))
            }

            findNavController().navigateUp() // Return to the list
        }

        return view
    }
}

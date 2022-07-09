package com.mustafacan.notes.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mustafacan.notes.databinding.FragmentNoteDetailBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.presentation.util.Status
import com.mustafacan.notes.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch


class NoteDetailFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        setObservers()

        binding.addNote.setOnClickListener {
            viewModel.addNote(
                Note(
                    binding.title.text.toString(),
                    binding.content.text.toString(),
                    System.currentTimeMillis()
                )
            )
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.addNoteSharedFlow.collectIndexed { _, value ->
                when (value.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "Note added successfully.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(), value.message ?: "Error", Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = NoteDetailFragment()
    }
}
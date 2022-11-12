package com.mustafacan.notes.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mustafacan.notes.R
import com.mustafacan.notes.databinding.FragmentNoteAddBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.presentation.util.Resource
import com.mustafacan.notes.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoteAddFragment : Fragment() {

    private var _binding: FragmentNoteAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        setListeners()
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.noteAddButton.setOnClickListener {
            viewModel.addNote(
                Note(
                    binding.noteAddTitle.text.toString(),
                    binding.noteAddContent.text.toString(),
                    System.currentTimeMillis()
                )
            )
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.addNoteSharedFlow.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.note_saved_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            result.message ?: getString(R.string.error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }
}
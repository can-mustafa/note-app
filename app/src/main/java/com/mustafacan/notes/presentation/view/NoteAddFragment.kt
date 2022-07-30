package com.mustafacan.notes.presentation.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mustafacan.notes.R
import com.mustafacan.notes.databinding.FragmentNoteAddBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.presentation.util.Status
import com.mustafacan.notes.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.flow.collectIndexed
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
        setHasOptionsMenu(true)
        _binding = FragmentNoteAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_note_add -> {
                viewModel.addNote(
                    Note(
                        binding.noteAddTitle.text.toString(),
                        binding.noteAddContent.text.toString(),
                        System.currentTimeMillis()
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.addNoteSharedFlow.collectIndexed { _, value ->
                when (value.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.note_saved_successfully),
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            value.message ?: getString(R.string.error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }
}
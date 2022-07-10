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
import androidx.navigation.fragment.navArgs
import com.mustafacan.notes.R
import com.mustafacan.notes.databinding.FragmentNoteEditBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.util.Util
import com.mustafacan.notes.presentation.util.Status
import com.mustafacan.notes.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NoteEditFragment : Fragment() {

    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotesViewModel

    private val args: NoteEditFragmentArgs by navArgs()
    private var noteId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        noteId = args.noteId

        if (noteId != Util.INVALID_NOTE_ID) {
            viewModel.getNoteById(noteId)
        }

        setListeners()
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.noteDetailSaveButton.setOnClickListener {
            if (noteId != Util.INVALID_NOTE_ID) {
                viewModel.addNote(
                    Note(
                        binding.noteDetailTitle.text.toString(),
                        binding.noteDetailContent.text.toString(),
                        System.currentTimeMillis(),
                        noteId
                    )
                )
            }
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.addNoteSharedFlow.collectIndexed { _, value ->
                when (value.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "Note saved successfully.",
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
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

        viewModel.getNoteLiveData.observe(viewLifecycleOwner) {
            bindData(it)
        }
    }

    private fun bindData(note: Note) {
        binding.noteDetailTitle.setText(note.title)
        binding.noteDetailContent.setText(note.content)

        val simpleDateFormat = SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault())
        val dateString = simpleDateFormat.format(note.timestamp)
        binding.noteDetailTimestamp.text = getString(R.string.last_update_date, dateString)
    }
}
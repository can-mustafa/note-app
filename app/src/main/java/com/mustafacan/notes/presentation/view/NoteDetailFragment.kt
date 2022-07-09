package com.mustafacan.notes.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.mustafacan.notes.databinding.FragmentNoteDetailBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.presentation.viewmodel.NotesViewModel
import java.text.SimpleDateFormat

class NoteDetailFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NotesViewModel

    private val args: NoteDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        val noteId = args.noteId
        viewModel.getNoteById(noteId)
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObservers() {
        viewModel.getNoteLiveData.observe(viewLifecycleOwner) {
            bindData(it)
        }
    }

    private fun bindData(note: Note) {
        binding.noteDetailTitle.text = note.title
        binding.noteDetailContent.text = note.content

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val dateString = simpleDateFormat.format(note.timestamp)
        binding.noteDetailTimestamp.text = dateString
    }

    companion object {
        fun newInstance() = NoteDetailFragment()
    }
}
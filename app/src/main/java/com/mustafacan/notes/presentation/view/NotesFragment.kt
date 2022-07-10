package com.mustafacan.notes.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mustafacan.notes.databinding.FragmentNotesBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.util.NoteOrder
import com.mustafacan.notes.domain.util.OrderType
import com.mustafacan.notes.presentation.adapter.NotesRecyclerViewAdapter
import com.mustafacan.notes.presentation.adapter.OnItemClickListener
import com.mustafacan.notes.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

class NotesFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NotesViewModel
    private lateinit var noteAdapter: NotesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        noteAdapter = NotesRecyclerViewAdapter(this)
        viewModel.getNotes()

        setListeners()
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(note: Note) {
        findNavController().navigate(
            NotesFragmentDirections.actionNoteListFragmentToNoteEditFragment(
                note.id ?: -1
            )
        )
    }

    override fun onRemoveClick(note: Note) {
        viewModel.deleteNote(note)
        Snackbar.make(requireView(), "Note deleted.", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                viewModel.restoreNote()
            }.show()
    }

    private fun setListeners() {
        binding.toggleSortView.setOnClickListener {
            with(binding.orderRadioGroup) {
                this.visibility = if (this.isVisible) View.GONE else View.VISIBLE
            }
        }

        binding.recyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.VERTICAL
                )
            )
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.HORIZONTAL
                )
            )
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = noteAdapter
        }

        binding.addFab.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNoteListFragmentToNoteAddFragment())
        }

        binding.orderByTitle.setOnClickListener {
            viewModel.getNotes(
                NoteOrder.Title(
                    if (binding.orderAscending.isChecked) OrderType.Ascending
                    else OrderType.Descending
                )
            )
        }
        binding.orderByDate.setOnClickListener {
            viewModel.getNotes(
                NoteOrder.Date(
                    if (binding.orderAscending.isChecked) OrderType.Ascending
                    else OrderType.Descending
                )
            )
        }
        binding.orderAscending.setOnClickListener {
            viewModel.getNotes(
                if (binding.orderByTitle.isChecked) NoteOrder.Title(OrderType.Ascending)
                else NoteOrder.Date(OrderType.Ascending)
            )
        }
        binding.orderDescending.setOnClickListener {
            viewModel.getNotes(
                if (binding.orderByTitle.isChecked) NoteOrder.Title(OrderType.Descending)
                else NoteOrder.Date(OrderType.Descending)
            )
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.getNotesSharedFlow.collectIndexed { _, value ->
                noteAdapter.notes = value
            }
        }
    }
}
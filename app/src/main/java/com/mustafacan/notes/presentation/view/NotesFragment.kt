package com.mustafacan.notes.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mustafacan.notes.R
import com.mustafacan.notes.databinding.FragmentNotesBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.util.NoteOrder
import com.mustafacan.notes.domain.util.OrderType
import com.mustafacan.notes.presentation.adapter.NotesRecyclerViewAdapter
import com.mustafacan.notes.presentation.adapter.OnItemClickListener
import com.mustafacan.notes.presentation.view.extensions.navigate
import com.mustafacan.notes.presentation.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels()
    private val noteAdapter by lazy { NotesRecyclerViewAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.toolbar_menu_note_sort -> {
                        with(binding.orderRadioGroup) {
                            visibility = if (isVisible) View.GONE else View.VISIBLE
                        }
                        true
                    }

                    else -> true
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.getNotes()

        setListeners()
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(note: Note) {
        navigate(
            NotesFragmentDirections.actionNoteListFragmentToNoteEditFragment(note.id ?: -1)
        )
    }

    override fun onRemoveClick(note: Note) {
        viewModel.deleteNote(note)
        Snackbar.make(requireView(), getString(R.string.note_deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                viewModel.restoreNote()
            }.show()
    }

    private fun setListeners() {
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
            navigate(NotesFragmentDirections.actionNoteListFragmentToNoteAddFragment())
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNotesSharedFlow.collectIndexed { _, value ->
                    _binding?.isEmptyList = value.isEmpty()
                    noteAdapter.submitList(value)
                }
            }
        }
    }
}
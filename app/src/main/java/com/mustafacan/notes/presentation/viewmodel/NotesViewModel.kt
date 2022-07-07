package com.mustafacan.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.repository.NoteRepository
import com.mustafacan.notes.domain.use_case.NoteUseCases
import com.mustafacan.notes.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases, private val repository: NoteRepository
) : ViewModel() {

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.addNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            noteUseCases.getNote(id)
        }
    }

    fun getNotes(): Flow<List<Note>> {
        return noteUseCases.getNotes()
    }
}
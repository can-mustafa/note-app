package com.mustafacan.notes.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.use_case.NoteUseCases
import com.mustafacan.notes.domain.util.NoteOrder
import com.mustafacan.notes.domain.util.OrderType
import com.mustafacan.notes.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _getNotesSharedFlow = MutableSharedFlow<List<Note>>()
    val getNotesSharedFlow: SharedFlow<List<Note>> get() = _getNotesSharedFlow

    private val _addNoteSharedFlow = MutableSharedFlow<Resource<Note>>()
    val addNoteSharedFlow: SharedFlow<Resource<Note>> get() = _addNoteSharedFlow

    private val _getNoteLiveData = MutableLiveData<Note>()
    val getNoteLiveData: LiveData<Note> get() = _getNoteLiveData

    private var recentlyDeletedNote: Note? = null

    fun addNote(note: Note) {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(note)
                _addNoteSharedFlow.emit(Resource.success(note))
            } catch (e: Exception) {
                _addNoteSharedFlow.emit(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
            recentlyDeletedNote = note
        }
    }

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            _getNoteLiveData.value = noteUseCases.getNote(id)
        }
    }

    fun getNotes(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)) {
        viewModelScope.launch {
            noteUseCases.getNotes(noteOrder).collectIndexed { _, value ->
                _getNotesSharedFlow.emit(value)
            }
        }
    }

    fun restoreNote() {
        viewModelScope.launch {
            addNote(recentlyDeletedNote ?: return@launch)
            recentlyDeletedNote = null
        }
    }
}
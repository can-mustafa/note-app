package com.mustafacan.notes.domain.use_case

import com.mustafacan.notes.domain.model.InvalidNoteException
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("Title can't be empty.")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("Content can't be empty.")
        }
        repository.addNote(note)
    }
}
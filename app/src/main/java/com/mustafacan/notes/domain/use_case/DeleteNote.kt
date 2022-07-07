package com.mustafacan.notes.domain.use_case

import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}
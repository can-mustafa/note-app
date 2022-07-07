package com.mustafacan.notes.domain.use_case

import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.domain.repository.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}
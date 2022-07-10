package com.mustafacan.notes.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotes,
    val getNote: GetNote,
    val deleteNote: DeleteNote,
    val addNote: AddNote
)
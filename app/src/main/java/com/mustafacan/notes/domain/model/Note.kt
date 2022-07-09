package com.mustafacan.notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
)

class InvalidNoteException(message: String) : Exception(message)
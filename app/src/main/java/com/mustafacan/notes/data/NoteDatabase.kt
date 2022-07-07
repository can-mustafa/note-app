package com.mustafacan.notes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mustafacan.notes.domain.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "note_db"
    }
}
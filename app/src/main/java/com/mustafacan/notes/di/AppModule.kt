package com.mustafacan.notes.di

import android.app.Application
import androidx.room.Room
import com.mustafacan.notes.data.NoteDao
import com.mustafacan.notes.data.NoteDatabase
import com.mustafacan.notes.domain.repository.NoteRepository
import com.mustafacan.notes.domain.repository.NoteRepositoryImpl
import com.mustafacan.notes.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(app: Application) = Room.databaseBuilder(
        app, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase) = database.noteDao()

    @Singleton
    @Provides
    fun provideNoteRepository(dao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            getNote = GetNote(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository)
        )
    }
}
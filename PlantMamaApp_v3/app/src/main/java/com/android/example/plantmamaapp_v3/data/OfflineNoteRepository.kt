package com.android.example.plantmamaapp_v3.data

import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(private val noteDao: NoteDao): NotesRepository  {
    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(note)
    }

    override suspend fun insertNotePhotoCrossRef(crossRef: NotePhotoCrossRef) {
        return noteDao.insertNotePhotoCrossRef(crossRef)
    }

    override fun getNotesForPlant(plantId: Int): Flow<List<NoteWithPhotos>> {
        return noteDao.getNotesForPlant(plantId)
    }

    override fun getFullPlantHistory(plantId: Int): Flow<PlantWithNotesAndPhotos> {
        return  noteDao.getFullPlantHistory(plantId)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNotePhotoCrossRefsForNote(note.noteId)
        return noteDao.deleteNote(note)
    }

    override fun getNoteWithPhotoStream(id: Int): Flow<NoteWithPhotos?> {
        return noteDao.getNoteWithPhotoStream(id)
    }

    override suspend fun getNoteWithPhoto(id: Int): NoteWithPhotos {
        return noteDao.getNoteWithPhoto(id)
    }

    override suspend fun updateNote(note: Note) {
        return noteDao.updateNote(note)
    }

    override suspend fun deleteNotePhotoCrossRefsForNote(noteId: Int) {
        return noteDao.deleteNotePhotoCrossRefsForNote(noteId)
    }
}
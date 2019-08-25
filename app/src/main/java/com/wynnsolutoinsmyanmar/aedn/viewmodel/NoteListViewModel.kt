package com.wynnsolutoinsmyanmar.aedn.viewmodel

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.wynnsolutoinsmyanmar.aedn.db.NoteDatabase
import com.wynnsolutoinsmyanmar.aedn.db.entity.Note

class NoteListViewModel(application: Application) : AndroidViewModel(application) {

    var listNote: LiveData<List<Note>>
    private val appDb: NoteDatabase

    init {
        appDb = NoteDatabase.getDataBase(this.getApplication())
        listNote = appDb.noteDao().getAllNotes()
    }

    fun getListNotes(): LiveData<List<Note>> {
        return listNote
    }

    fun addNote(note: Note) {
        addAsynTask(appDb).execute(note)
    }


    class addAsynTask(db: NoteDatabase) : AsyncTask<Note, Void, Void>() {
        private var noteDb = db
        override fun doInBackground(vararg params: Note): Void? {
            noteDb.noteDao().insert(params[0])
            return null
        }

    }

}
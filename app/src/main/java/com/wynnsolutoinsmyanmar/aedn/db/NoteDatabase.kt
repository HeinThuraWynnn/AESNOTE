package com.wynnsolutoinsmyanmar.aedn.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wynnsolutoinsmyanmar.aedn.db.dao.NoteDao
import com.wynnsolutoinsmyanmar.aedn.db.entity.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: NoteDatabase? = null
        fun getDataBase(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"
                )
                    .allowMainThreadQueries().build()
            }
            return INSTANCE as NoteDatabase
        }
    }

    abstract fun noteDao(): NoteDao

}
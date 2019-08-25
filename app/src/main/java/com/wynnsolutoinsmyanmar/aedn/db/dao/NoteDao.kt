package com.wynnsolutoinsmyanmar.aedn.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wynnsolutoinsmyanmar.aedn.db.entity.Note

@Dao
interface NoteDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)
    @Update
    fun update(note: Note)
    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("select * from notes_table where idNote in (:id)")
    fun getById(id: Int): Note

    @Query("DELETE FROM notes_table")
    fun deleteAllNotes()

}
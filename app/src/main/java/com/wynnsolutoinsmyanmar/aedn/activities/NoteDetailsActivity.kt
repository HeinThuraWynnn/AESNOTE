package com.wynnsolutoinsmyanmar.aedn.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.wynnsolutoinsmyanmar.aedn.R
import com.wynnsolutoinsmyanmar.aedn.db.NoteDatabase
import com.wynnsolutoinsmyanmar.aedn.db.dao.NoteDao
import com.wynnsolutoinsmyanmar.aedn.db.entity.Note
import com.wynnsolutoinsmyanmar.aedn.viewmodel.NoteListViewModel
import kotlinx.android.synthetic.main.activity_note_details.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec




class NoteDetailsActivity : AppCompatActivity() {


    private var daoNote: NoteDao? = null
    private var viewModel: NoteListViewModel? = null

    private var currentNote: Int? = null
    private var note: Note? = null

    val AES = "AES"
    var encDesc: String = ""
    var decDesc: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)
        var db: NoteDatabase = NoteDatabase.getDataBase(this)

        daoNote = db.noteDao()

        viewModel = ViewModelProviders.of(this).get(NoteListViewModel::class.java)
        currentNote = intent.getIntExtra("idNote", -1)
        if (currentNote != -1) {
            setTitle(R.string.edit_note_title)
            note = daoNote!!.getById(currentNote!!)
            title_edit_text.setText(note!!.title)
            desc_edit_text.setText(note!!.description)

            // Dec Btn Assign
            val btnDec = findViewById(R.id.btnDec) as Button
            // set on-click listener
            btnDec.setOnClickListener {
                btnDec()
            }
        } else {
            setTitle(R.string.add_note_title)
            invalidateOptionsMenu()
        }
        if (currentNote == -1) {
            btnDec.visibility = View.GONE
        }
        // get reference to button
        val btnEnc = findViewById(R.id.btnEnc) as Button
        // set on-click listener
        btnEnc.setOnClickListener {
            btnEnc()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.done_item -> {
                if (currentNote == -1) {
                    saveNote()
                } else {
                    updateNote()
                    Toast.makeText(this, getString(R.string.update_note), Toast.LENGTH_SHORT).show()
                }

                finish()
            }
            R.id.delete_item -> {
                deleteNote()
                Toast.makeText(this, getString(R.string.delete_note), Toast.LENGTH_SHORT).show()
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        if (currentNote == -1) {
            menu.findItem(R.id.delete_item).isVisible = false
        }
        return true
    }

    private fun saveNote() {
        if (title_edit_text.text.toString().trim().isBlank() || desc_edit_text.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Can not insert empty note!", Toast.LENGTH_SHORT).show()
            return
        }

        var titleNote = title_edit_text.text.toString()
        var descriptionNote = desc_edit_text.text.toString()
        var pwd = passwrord.text.toString()
        var note = Note(0, titleNote, descriptionNote)
        viewModel!!.addNote(note)
        Toast.makeText(this, getString(R.string.save_note), Toast.LENGTH_SHORT).show()


    }

    private fun deleteNote() {
        daoNote!!.delete(note!!)
    }

    private fun updateNote() {
        var titleNote = title_edit_text.text.toString()
        var descriptionNote = desc_edit_text.text.toString()
        var pwd = passwrord.text.toString()
        var note = Note(note!!.id, titleNote, descriptionNote)
        daoNote!!.update(note)
    }


    /// Enc Dec Functions ///
    private fun btnEnc(){
        encDesc  = encrypt(desc_edit_text.text.toString(),passwrord.text.toString())
        desc_edit_text.setText(encDesc)
        Toast.makeText(this,"Encrypted",Toast.LENGTH_LONG).show()
    }



    private fun btnDec(){
        try {
            decDesc = decrypt(desc_edit_text.text.toString(), passwrord.text.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        desc_edit_text.setText(decDesc)
        Toast.makeText(this,"Decrypted",Toast.LENGTH_LONG).show()

    }
    @Throws(Exception::class)
    private fun decrypt(outputString: String, password: String): String {
        val key = generateKey(password)
        val c = Cipher.getInstance(AES)
        c.init(Cipher.DECRYPT_MODE, key)
        val decodedValue = Base64.decode(outputString, Base64.DEFAULT)
        val decValue = c.doFinal(decodedValue)
        return String(decValue)
    }

    @Throws(Exception::class)
    private fun encrypt(Data: String, password: String): String {
        val key = generateKey(password)
        val c = Cipher.getInstance(AES)
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(Data.toByteArray())
        return Base64.encodeToString(encVal, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }

}

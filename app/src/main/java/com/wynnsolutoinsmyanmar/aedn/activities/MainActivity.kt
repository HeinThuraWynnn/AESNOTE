package com.wynnsolutoinsmyanmar.aedn.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wynnsolutoinsmyanmar.aedn.R
import com.wynnsolutoinsmyanmar.aedn.adapter.NoteRecyclerAdapter
import com.wynnsolutoinsmyanmar.aedn.db.NoteDatabase
import com.wynnsolutoinsmyanmar.aedn.db.entity.Note
import com.wynnsolutoinsmyanmar.aedn.viewmodel.NoteListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),NoteRecyclerAdapter.OnItemClickListener{

    private var noteRecyclerView: RecyclerView? = null
    private var recyclerViewAdapter: NoteRecyclerAdapter? = null
    private var viewModel: NoteListViewModel? = null
    private var db: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = NoteDatabase.getDataBase(this)

        noteRecyclerView = findViewById(R.id.recycler_view)
        recyclerViewAdapter = NoteRecyclerAdapter(arrayListOf(), this)

        noteRecyclerView!!.layoutManager = LinearLayoutManager(this)
        noteRecyclerView!!.adapter = recyclerViewAdapter

        viewModel = ViewModelProviders.of(this).get(NoteListViewModel::class.java)

        viewModel!!.getListNotes().observe(this, Observer { notes ->
            recyclerViewAdapter!!.addNotes(notes!!)
        })
        buttonAddNote.setOnClickListener {
            var intent = Intent(applicationContext, NoteDetailsActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_items -> {
                deleteAllNotes()
            }
        }
        when (item.itemId) {
            R.id.update_pass_code -> {
                val intent = Intent(this@MainActivity, ResetPinCode::class.java)
                this@MainActivity.startActivity(intent)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotes() {
        db!!.noteDao().deleteAllNotes()
    }

    override fun onItemClick(note: Note) {
        var intent = Intent(applicationContext, NoteDetailsActivity::class.java)
        intent.putExtra("idNote", note.id)
        startActivity(intent)
    }
}


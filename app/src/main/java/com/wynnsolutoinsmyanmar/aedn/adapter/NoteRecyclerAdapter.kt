package com.wynnsolutoinsmyanmar.aedn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wynnsolutoinsmyanmar.aedn.R
import com.wynnsolutoinsmyanmar.aedn.activities.MainActivity
import com.wynnsolutoinsmyanmar.aedn.db.entity.Note

class NoteRecyclerAdapter(notes: ArrayList<Note>, listener: OnItemClickListener) : RecyclerView.Adapter<NoteRecyclerAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_list, parent, false))
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var currentNote: Note = listNotes[position]

        var title = currentNote.title
        var description = currentNote.description

        holder!!.mTitle.text = title
        holder!!.mDescription.text = description

        holder.bind(currentNote, listenerNote)

    }

    private var listNotes: List<Note> = notes

    private var listenerNote: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    fun addNotes(listNotes: List<Note>) {
        this.listNotes = listNotes
        notifyDataSetChanged()
    }


    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTitle = itemView.findViewById<TextView>(R.id.title_note)!!
        var mDescription = itemView.findViewById<TextView>(R.id.desc_note)!!

        fun bind(note: Note, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(note)
            }
        }

    }
}
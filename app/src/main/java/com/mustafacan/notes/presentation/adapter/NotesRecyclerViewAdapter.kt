package com.mustafacan.notes.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mustafacan.notes.R
import com.mustafacan.notes.domain.model.Note

class NotesRecyclerViewAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Note, NotesRecyclerViewAdapter.NoteViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val colors: Array<String> = arrayOf(
        "#8FBCBB", "#A3BE8C", "#7AC3A1", "#B48EAD", "#81A1C1", "#8BD184", "#CC927C"
    )

    var notes: List<Note>
        get() = currentList
        set(value) = submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_row, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val titleTextView = holder.itemView.findViewById<TextView>(R.id.note_title)
        val contentTextView = holder.itemView.findViewById<TextView>(R.id.note_content)
        val noteDeleteImageView = holder.itemView.findViewById<ImageView>(R.id.note_delete)

        val note = notes[position]

        holder.itemView.apply {
            setBackgroundColor(Color.parseColor(colors[position % colors.size]))
            setOnClickListener { listener.onItemClick(note) }
            titleTextView.text = note.title
            contentTextView.text = note.content
            noteDeleteImageView.setOnClickListener { listener.onRemoveClick(note) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(note: Note)
    fun onRemoveClick(note: Note)
}
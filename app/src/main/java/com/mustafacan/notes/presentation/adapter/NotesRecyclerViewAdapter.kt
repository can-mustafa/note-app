package com.mustafacan.notes.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mustafacan.notes.databinding.ItemNoteRowBinding
import com.mustafacan.notes.domain.model.Note
import com.mustafacan.notes.presentation.view.extensions.setOnSafeClickListener

class NotesRecyclerViewAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Note, NotesRecyclerViewAdapter.NoteViewHolder>(
        DIFF_CALLBACK
    ) {

    private val colors: Array<String> = arrayOf(
        "#8FBCBB", "#A3BE8C", "#7AC3A1", "#B48EAD", "#81A1C1", "#8BD184", "#CC927C"
    )

    var notes: List<Note>
        get() = currentList
        set(value) = submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(private val binding: ItemNoteRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note) {
            binding.run {
                root.setBackgroundColor(Color.parseColor(colors[adapterPosition % colors.size]))
                root.setOnSafeClickListener {
                    listener.onItemClick(item)
                }
                noteTitle.text = item.title
                noteContent.text = item.content
                noteDelete.setOnSafeClickListener {
                    listener.onRemoveClick(item)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(note: Note)
    fun onRemoveClick(note: Note)
}
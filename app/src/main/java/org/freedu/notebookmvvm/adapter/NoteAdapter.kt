package org.freedu.notebookmvvm.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.freedu.notebookmvvm.MainActivity
import org.freedu.notebookmvvm.Repository.NoteRepository
import org.freedu.notebookmvvm.database.NoteDatabase
import org.freedu.notebookmvvm.databinding.NoteLayoutBinding
import org.freedu.notebookmvvm.fragment.HomeFragmentDirections
import org.freedu.notebookmvvm.model.Note
import org.freedu.notebookmvvm.viewmodel.NoteViewModel
import org.freedu.notebookmvvm.viewmodel.NoteViewModelFactory
import java.util.Random

class NoteAdapter(private val onDeleteNoteListener: OnDeleteNoteListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(val itemBinding: NoteLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id && oldItem.noteBody == newItem.noteBody && oldItem.noteTitle == newItem.noteTitle
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        val random = Random()
        val color = Color.argb(
            255,
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        )
        holder.itemBinding.apply {
            noteTitle.text = currentNote.noteTitle
            noteBody.text = currentNote.noteBody
            ibColor.setBackgroundColor(color)
        }
        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
        holder.itemView.setOnLongClickListener {
            val currentNote = differ.currentList[position]
            onDeleteNoteListener.showDeleteConfirmationDialog(currentNote)
            true // Returning true to indicate that the long click event is consumed
        }

    }
    interface OnDeleteNoteListener {
        fun showDeleteConfirmationDialog(note: Note)
    }
}
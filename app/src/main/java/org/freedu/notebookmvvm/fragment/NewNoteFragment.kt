package org.freedu.notebookmvvm.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.freedu.notebookmvvm.R
import org.freedu.notebookmvvm.Repository.NoteRepository
import org.freedu.notebookmvvm.database.NoteDatabase
import org.freedu.notebookmvvm.databinding.FragmentNewNoteBinding
import org.freedu.notebookmvvm.model.Note
import org.freedu.notebookmvvm.viewmodel.NoteViewModel
import org.freedu.notebookmvvm.viewmodel.NoteViewModelFactory


class NewNoteFragment : Fragment() {
    private lateinit var binding: FragmentNewNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        val noteDatabase = NoteDatabase(requireContext())
        val repository = NoteRepository(noteDatabase)
        val factory = NoteViewModelFactory(requireActivity().application, repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        setHasOptionsMenu(true)
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val noteTitle = binding.noteTitleEt.text.toString().trim()
        val noteBody = binding.noteBodyEt.text.toString().trim()

        if (noteTitle.isNotEmpty() && noteBody.isNotEmpty()) {
            val note = Note(noteTitle =  noteTitle, noteBody = noteBody)
            noteViewModel.addNote(note)
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Please enter a note title and body", Toast.LENGTH_SHORT).show()
        }
    }


}
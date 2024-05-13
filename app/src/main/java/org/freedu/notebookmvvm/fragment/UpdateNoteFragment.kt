package org.freedu.notebookmvvm.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.freedu.notebookmvvm.R
import org.freedu.notebookmvvm.Repository.NoteRepository
import org.freedu.notebookmvvm.database.NoteDatabase
import org.freedu.notebookmvvm.databinding.FragmentUpdateNoteBinding
import org.freedu.notebookmvvm.model.Note
import org.freedu.notebookmvvm.viewmodel.NoteViewModel
import org.freedu.notebookmvvm.viewmodel.NoteViewModelFactory

class UpdateNoteFragment : Fragment() {
    private lateinit var binding: FragmentUpdateNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private var currentNote: Note? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        val noteDatabase = NoteDatabase(requireContext())
        val repository = NoteRepository(noteDatabase)
        val factory = NoteViewModelFactory(requireActivity().application, repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        arguments?.let {
            currentNote = UpdateNoteFragmentArgs.fromBundle(it).note
            currentNote?.let { note ->
                binding.noteTitleEt.setText(note.noteTitle)
                binding.noteBodyEt.setText(note.noteBody)
            }
        }

        binding.fabBtn.setOnClickListener {
            val noteTitle = binding.noteTitleEt.text.toString().trim()
            val noteBody = binding.noteBodyEt.text.toString().trim()

            if (noteTitle.isNotEmpty() && noteBody.isNotEmpty()) {
                currentNote?.let { note ->
                    val updatedNote = note.copy(noteTitle = noteTitle, noteBody = noteBody)
                    noteViewModel.updateNote(updatedNote)
                    requireActivity().onBackPressed()
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a note title and body", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}
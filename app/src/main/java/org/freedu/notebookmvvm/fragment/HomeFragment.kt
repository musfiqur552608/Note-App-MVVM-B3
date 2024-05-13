package org.freedu.notebookmvvm.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.freedu.notebookmvvm.R
import org.freedu.notebookmvvm.Repository.NoteRepository
import org.freedu.notebookmvvm.adapter.NoteAdapter
import org.freedu.notebookmvvm.database.NoteDatabase
import org.freedu.notebookmvvm.databinding.FragmentHomeBinding
import org.freedu.notebookmvvm.viewmodel.NoteViewModel
import org.freedu.notebookmvvm.viewmodel.NoteViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteRepository: NoteRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val noteDatabase = NoteDatabase(requireContext())
        val repository = NoteRepository(noteDatabase)
        val factory = NoteViewModelFactory(requireActivity().application, repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        val adapter = NoteAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        noteViewModel.getAllNotes().observe(viewLifecycleOwner, { notes ->
            notes?.let {
                adapter.differ.submitList(it)
                if (it.isEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    binding.noNoteAvailable.visibility = View.VISIBLE
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noNoteAvailable.visibility = View.GONE
                }
            }
        })

        binding.fabBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToNewNoteFragment()
            findNavController().navigate(action)
        }


        return binding.root
    }


}
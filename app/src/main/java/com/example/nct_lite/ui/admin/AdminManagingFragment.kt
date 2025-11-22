package com.example.nct_lite.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nct_lite.databinding.AdminmanagingBinding
import com.example.nct_lite.ui.activity.MainActivity
import com.example.nct_lite.ui.admin.adapter.AdminManageAdapter
import com.example.nct_lite.viewmodel.admin.AdminSongViewModel

class AdminManagingFragment : Fragment() {

    private var _binding: AdminmanagingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { ViewModelProvider(this)[AdminSongViewModel::class.java] }
    private val adapter by lazy {
        AdminManageAdapter(
            onPreview = {
                (activity as? MainActivity)?.openAdminPreview()
            },
            onEdit = { song ->
                (activity as? MainActivity)?.openAdminEditSong(song)
            },
            onReject = { viewModel.rejectSong(it._id) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminmanagingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.button12.setOnClickListener {
            (activity as? MainActivity)?.openAdminPreview()
        }

        viewModel.pendingSongs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                adapter.submitList(response.metadata)
            }
            result.onFailure {
                Toast.makeText(requireContext(), it.message ?: "Failed to load songs", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.actionResult.observe(viewLifecycleOwner) { result ->
            result.onFailure {
                Toast.makeText(requireContext(), it.message ?: "Action failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadPendingSongs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

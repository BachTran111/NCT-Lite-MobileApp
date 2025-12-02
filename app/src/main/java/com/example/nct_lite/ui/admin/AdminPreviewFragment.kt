package com.example.nct_lite.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nct_lite.databinding.AdminpreviewBinding
import com.example.nct_lite.viewmodel.admin.AdminSongViewModel
import com.example.nct_lite.ui.admin.adapter.AdminPreviewAdapter

class AdminPreviewFragment : Fragment() {

    private var _binding: AdminpreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { ViewModelProvider(this)[AdminSongViewModel::class.java] }
    private val adapter by lazy {
        AdminPreviewAdapter(
            onApprove = { viewModel.approveSong(it._id) },
            onReject = { viewModel.rejectSong(it._id) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminpreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.pendingSongs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                adapter.submitList(response.metadata)
            }
            result.onFailure {
                Toast.makeText(requireContext(), it.message ?: "Failed to load songs", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.actionResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Successed to action", Toast.LENGTH_SHORT).show()
            }
            result.onFailure {
                Toast.makeText(requireContext(), it.message ?: "Failed to action", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadPendingSongs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

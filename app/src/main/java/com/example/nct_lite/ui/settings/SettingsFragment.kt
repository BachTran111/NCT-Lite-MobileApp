package com.example.nct_lite.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nct_lite.databinding.SettingsBinding
import com.example.nct_lite.ui.activity.MainActivity
import com.example.nct_lite.ui.activity.UserUploadMusicActivity

class SettingsFragment : Fragment() {

    private var _binding: SettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonback.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.button38.setOnClickListener {
            startActivity(Intent(requireContext(), UserUploadMusicActivity::class.java))
        }

        binding.btnAdmin.setOnClickListener {
            (activity as? MainActivity)?.openAdminPanel()
        }
        val isAdmin = (activity as? MainActivity)?.isAdmin() == true
        binding.btnAdmin.visibility = if (isAdmin) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

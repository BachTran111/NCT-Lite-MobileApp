package com.example.nct_lite.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nct_lite.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupSearchFunctionality()
        setupSearchSuggestions()
    }

    private fun setupSearchFunctionality() {
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    // TODO: Gọi API search khi database đã connect
                    // searchViewModel.search(query)
                    showSearchResults(query)
                } else {
                    hideSearchResults()
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSearchSuggestions() {
        // Gợi ý tìm kiếm mặc định
        val suggestions = listOf(
            "Nhạc Pop",
            "Nhạc Rock",
            "Nhạc Rap",
            "Nhạc EDM",
            "Nhạc Ballad",
            "Nhạc Jazz"
        )

        val container = binding.containerSearchSuggestions
        suggestions.forEach { suggestion ->
            val suggestionView = TextView(context).apply {
                text = suggestion
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(16, 12, 16, 12)
                setBackgroundColor(0x33FFFFFF.toInt()) // Màu trắng trong suốt
                setOnClickListener {
                    binding.editSearch.setText(suggestion)
                    // TODO: Gọi API search khi database đã connect
                }
            }
            container.addView(suggestionView)
        }
    }

    private fun showSearchResults(query: String) {
        binding.textSearchResults.visibility = View.VISIBLE
        binding.textSearchResults.text = "Kết quả tìm kiếm: \"$query\""
        
        // TODO: Hiển thị kết quả tìm kiếm từ API
        // Hiện tại chỉ hiển thị placeholder
        val container = binding.containerSearchResults
        container.removeAllViews()
        
        val placeholderView = TextView(context).apply {
            text = "Đang tìm kiếm \"$query\"...\n\n(Kết quả sẽ hiển thị khi database đã kết nối)"
            textSize = 14f
            setTextColor(resources.getColor(android.R.color.darker_gray))
            setPadding(16, 16, 16, 16)
        }
        container.addView(placeholderView)
    }

    private fun hideSearchResults() {
        binding.textSearchResults.visibility = View.GONE
        binding.containerSearchResults.removeAllViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

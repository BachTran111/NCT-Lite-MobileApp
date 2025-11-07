package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nct_lite.R

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val etSearch = view.findViewById<EditText>(R.id.et_search)
        val tvResult = view.findViewById<TextView>(R.id.tv_search_result)

        // Giả lập hiển thị kết quả khi người dùng nhập
        etSearch.setOnEditorActionListener { v, _, _ ->
            tvResult.text = "Kết quả tìm kiếm cho: ${v.text}"
            true
        }

        return view
    }
}

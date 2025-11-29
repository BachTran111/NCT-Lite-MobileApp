package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nct_lite.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSelectedFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.action_add_to_playlist)?.setOnClickListener {
            AddToPlaylistBottomSheetFragment().show(parentFragmentManager, "AddToPlaylistBottomSheet")
        }
        view.findViewById<View>(R.id.action_share)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.action_add_to_liked)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.action_download)?.setOnClickListener { dismiss() }
    }
}


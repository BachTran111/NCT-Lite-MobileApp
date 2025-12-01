package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nct_lite.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddToPlaylistBottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_to_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.btn_new_playlist)?.setOnClickListener {
            NewPlaylistBottomSheet().show(parentFragmentManager, "NewPlaylistBottomSheet")
        }
        view.findViewById<View>(R.id.btn_done)?.setOnClickListener {
            dismiss()
        }
    }
}

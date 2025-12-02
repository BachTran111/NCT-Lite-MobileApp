package com.example.nct_lite.data.history.response

import com.example.nct_lite.data.history.model.HistoryItem
import com.example.nct_lite.data.history.model.PlayHistory

data class HistoryListResponse(
    val status: String,
    val message: String,
    val metadata: List<HistoryItem>
)



package com.example.nct_lite.data.history.response

import com.example.nct_lite.data.history.model.PlayHistory

data class PlayHistoryResponse(
    val status: String,
    val message: String,
    val metadata: PlayHistory
)

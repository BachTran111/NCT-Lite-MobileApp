package com.example.nct_lite.data.remote.model.response

import com.example.nct_lite.data.remote.model.PlayHistory

data class PlayHistoryResponse(
    val status: String,
    val message: String,
    val metadata: PlayHistory
)

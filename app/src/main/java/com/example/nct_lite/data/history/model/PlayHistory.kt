package com.example.nct_lite.data.history.model

import com.example.nct_lite.data.song.model.Song

data class HistoryItem(
    val song: Song,
    val playedAt: String
)

data class PlayHistory(
    val _id: String,
    val userId: String,
    val items: List<HistoryItem>
)

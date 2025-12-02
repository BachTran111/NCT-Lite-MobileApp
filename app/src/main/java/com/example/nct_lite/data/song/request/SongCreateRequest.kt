package com.example.nct_lite.data.song.request

class SongCreateRequest (
    val title: String,
    val artist: String,
    val genreIDs: List<String>,
    val url: String,
    val coverUrl: String,
    val uploaderId: String
)

//title,
//artist,
//genreIDs,
//url,
//coverUrl,
//uploaderId,
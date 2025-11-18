package com.example.nct_lite.data.model

data class Song(
  var id: String = "",
  var title: String = "",
  var artist: String = "",
  var genreIDs: List<String> = emptyList(),
  var url: String = "",
  var coverUrl: String = "",
  var uploaderId: String = "",
  var createdAt: String = "",
  var audioUrl: String = ""
)
package com.example.nct_lite.data.remote.model

data class User(
    val _id: String,
    val username: String,
    val role: String,
    val avatarUrl: String,
    val playlistsCount: Int,
    val followersCount: Int,
    val followingCount: Int
)

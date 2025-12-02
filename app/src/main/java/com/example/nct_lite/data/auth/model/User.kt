package com.example.nct_lite.data.auth.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val _id: String,
    val username: String,
    val role: String
//    val token: String
)
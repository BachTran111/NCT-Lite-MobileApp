package com.example.nct_lite.data.genre.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val _id: String,
    val name: String
) : Parcelable
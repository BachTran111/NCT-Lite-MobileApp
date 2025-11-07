package com.example.nct_lite.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.model.Genre
import com.example.nct_lite.ui.adapter.GenreAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerGenres: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        recyclerGenres = findViewById(R.id.grid_genres)

        val genres = listOf(
            Genre("Pop"),
            Genre("Rock"),
            Genre("Jazz"),
            Genre("Hip-Hop"),
            Genre("Indie"),
            Genre("K-Pop")
        )

        val adapter = GenreAdapter(genres) { genre ->
            // TODO: xử lý khi click, ví dụ mở danh sách bài hát
        }

        recyclerGenres.layoutManager = GridLayoutManager(this, 2)
        recyclerGenres.adapter = adapter
    }
}

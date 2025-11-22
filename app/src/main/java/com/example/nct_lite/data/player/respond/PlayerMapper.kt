package com.example.nct_lite.data.player.respond
import com.example.nct_lite.data.song.response.SongMetadata

fun SongMetadata.toPlayerSong(): PlayerSong {
    return PlayerSong(
        id = this._id,
        title = this.title,
        artist = this.artist,
        url = this.url,
        coverUrl = this.coverUrl ?: ""
    )
}

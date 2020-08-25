package com.amartinez.musicplayer.data.repository

import com.amartinez.musicplayer.domain.model.Search
import io.reactivex.Observable

interface Repository {
    fun search(
        term: String,
        type: String,
        limit: Int
    ): Observable<Search>

    fun loadAlbum(
        id: Long,
        entity: String
    ): Observable<Search>
}
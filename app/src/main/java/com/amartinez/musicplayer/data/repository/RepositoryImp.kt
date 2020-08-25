package com.amartinez.musicplayer.data.repository

import com.amartinez.musicplayer.data.remote.Api
import com.amartinez.musicplayer.data.repository.mapper.entity.SearchResponseToDomainMapper
import com.amartinez.musicplayer.domain.model.Search
import io.reactivex.Observable

class RepositoryImp(private val api: Api, private val mapper: SearchResponseToDomainMapper) : Repository {
    override fun search(
        term: String,
        type: String,
        limit: Int
    ): Observable<Search> {
        return api.search(term, type, limit).map {
            mapper.map(it)
        }
    }

    override fun loadAlbum(id: Long, entity: String): Observable<Search> {
        return api.loadAlbum(id, entity).map {
            mapper.map(it)
        }
    }
}
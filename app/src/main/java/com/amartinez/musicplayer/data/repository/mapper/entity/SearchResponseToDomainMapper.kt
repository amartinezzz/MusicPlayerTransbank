package com.amartinez.musicplayer.data.repository.mapper.entity

import com.amartinez.musicplayer.data.remote.response.SearchResponse
import com.amartinez.musicplayer.data.repository.mapper.entity.ResultEntityToDomainMapper
import com.amartinez.musicplayer.domain.model.Search
import javax.inject.Inject

class SearchResponseToDomainMapper @Inject constructor(private val mapper: ResultEntityToDomainMapper) {

    fun map(value: SearchResponse): Search {
        val search = Search()
        search.results = mapper.map(value.results)
        return search
    }

    fun reverseMap(value: Search): SearchResponse {
        throw UnsupportedOperationException()
    }
}
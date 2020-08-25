package com.amartinez.musicplayer.data.repository.mapper.local

import com.amartinez.musicplayer.data.local.SearchLocal
import com.amartinez.musicplayer.domain.model.Search
import io.realm.RealmList
import javax.inject.Inject

class SearchLocalToDomainMapper @Inject constructor(private val mapper: ResultLocalToDomainMapper) {

    fun map(value: SearchLocal): Search {
        val search = Search()
        search.term = value.term
        search.results = mapper.map(value.results)
        return search
    }

    fun reverseMap(value: Search): SearchLocal {
        val result = SearchLocal()
        result.term = value.term
        result.results = RealmList()
        result.results.addAll(value.results.map {
            mapper.reverseMap(it)
        })
        return result
    }
}
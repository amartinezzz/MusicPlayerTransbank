package com.amartinez.musicplayer.data.repository.mapper.local

import com.amartinez.musicplayer.data.local.ResultLocal
import com.amartinez.musicplayer.domain.model.Result
import io.realm.RealmList
import javax.inject.Inject

class ResultLocalToDomainMapper @Inject constructor() {

    fun map(value: ResultLocal): Result {
        return Result(value.kind, value.artistId, value.collectionId, value.trackId,
            value.artistName, value.collectionName, value.trackName, value.artistViewUrl,
        value.trackViewUrl, value.previewUrl, value.artworkUrl30, value.artworkUrl60,
        value.artworkUrl100, value.isStreamable)
    }

    fun reverseMap(value: Result): ResultLocal {
        val result = ResultLocal()
        result.kind = value.kind
        result.artistId = value.artistId
        result.collectionId = value.collectionId
        result.trackId = value.trackId
        result.artistName = value.artistName
        result.collectionName = value.collectionName
        result.trackName = value.trackName
        result.artistViewUrl = value.artistViewUrl
        result.trackViewUrl = value.trackViewUrl
        result.previewUrl = value.previewUrl
        result.artworkUrl30 = value.artworkUrl30
        result.artworkUrl60 = value.artworkUrl60
        result.artworkUrl100 = value.artworkUrl100
        result.isStreamable = value.isStreamable

        return result
    }

    fun map(value: RealmList<ResultLocal>) : ArrayList<Result> {
        val list = ArrayList<Result>()
        list.addAll(value.map {
            map(it)
        })

        return list
    }

    fun reverseMap(value: List<Result>) : RealmList<ResultLocal> {
        val list = RealmList<ResultLocal>()
        list.addAll(value.map {
            reverseMap(it)
        })

        return list
    }
}
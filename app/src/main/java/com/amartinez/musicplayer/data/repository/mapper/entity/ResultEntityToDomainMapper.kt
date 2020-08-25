package com.amartinez.musicplayer.data.repository.mapper.entity

import com.amartinez.musicplayer.data.entity.ResultEntity
import com.amartinez.musicplayer.domain.model.Result
import javax.inject.Inject

class ResultEntityToDomainMapper @Inject constructor() {

    fun map(value: ResultEntity): Result {
        return Result(value.kind, value.artistId,
            value.collectionId, value.trackId, value.artistName, value.collectionName,
            value.trackName, value.artistViewUrl, value.trackViewUrl, value.previewUrl,
            value.artworkUrl30,value.artworkUrl60,value.artworkUrl100, value.isStreamable)
    }

    fun reverseMap(value: Result): ResultEntity? {
        throw UnsupportedOperationException()
    }

    fun map(value: List<ResultEntity>) : ArrayList<Result> {
        val list = ArrayList<Result>()
        list.addAll(value.map {
            map(it)
        })

        return list
    }
}
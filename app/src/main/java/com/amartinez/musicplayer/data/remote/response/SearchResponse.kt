package com.amartinez.musicplayer.data.remote.response

import com.amartinez.musicplayer.data.entity.ResultEntity

class SearchResponse {
    var resultCount: Long = 0
    var results: List<ResultEntity> = ArrayList()
}
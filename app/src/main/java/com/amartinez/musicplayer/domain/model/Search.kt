package com.amartinez.musicplayer.domain.model

class Search(
    var term: String = "",
    var resultCount: Long = 0,
    var results: List<Result> = ArrayList(),
    var error: Boolean = false
)
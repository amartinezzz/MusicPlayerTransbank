package com.amartinez.musicplayer.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ResultLocal(
    @PrimaryKey
    var id:Int = 0,
    var kind: String = "",
    var artistId: Long = 0,
    var collectionId: Long = 0,
    var trackId: Long = 0,
    var artistName: String = "",
    var collectionName: String = "",
    var trackName: String = "",
    var artistViewUrl: String = "",
    var trackViewUrl: String = "",
    var previewUrl: String = "",
    var artworkUrl30: String = "",
    var artworkUrl60: String = "",
    var artworkUrl100: String = "",
    var isStreamable: Boolean = false
) : RealmObject()
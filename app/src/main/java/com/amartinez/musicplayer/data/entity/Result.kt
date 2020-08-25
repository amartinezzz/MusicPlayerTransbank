package com.amartinez.musicplayer.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class ResultEntity (
    var wrapperType: String = "",
    var kind: String = "",
    var artistId: Long = 0,
    var collectionId: Long = 0,
    var trackId: Long = 0,
    var artistName: String = "",
    var collectionName: String = "",
    var trackName: String = "",
    var collectionCensoredName: String = "",
    var trackCensoredName: String = "",
    var artistViewUrl: String = "",
    var collectionViewUrl: String = "",
    var trackViewUrl: String = "",
    var previewUrl: String = "",
    var artworkUrl30: String = "",
    var artworkUrl60: String = "",
    var artworkUrl100: String = "",
    var collectionPrice: Float = 0F,
    var trackPrice: Float = 0F,
    var releaseDate: Date = Date(),
    var collectionExplicitness: String = "",
    var trackExplicitness: String = "",
    var discCount: Int = 0,
    var discNumber: Int = 0,
    var trackCount: Int = 0,
    var trackNumber: Int = 0,
    var trackTimeMillis: Long = 0,
    var country: String = "",
    var currency: String = "",
    var primaryGenreName: String = "",
    var isStreamable: Boolean = false
) : Parcelable
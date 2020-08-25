package com.amartinez.musicplayer.presentation.detail

import com.amartinez.musicplayer.domain.model.Result

interface DetailContract {
    interface View {
        fun displaySongs(results: List<Result>)
        fun showError()
    }

    interface Presenter {
        fun initialize(view: DetailContract.View)
        fun loadAlbum(id: Long, isNetworkConnected: Boolean)
    }
}
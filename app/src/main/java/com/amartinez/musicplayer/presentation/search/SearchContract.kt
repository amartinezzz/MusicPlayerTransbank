package com.amartinez.musicplayer.presentation.search

import com.amartinez.musicplayer.domain.model.Result

interface SearchContract {
    interface View {
        fun displaySearchResults(results: List<Result>)
        fun showError()
    }

    interface Presenter {
        fun initialize(view: SearchContract.View)
        fun search(term: String, isNetworkConnected: Boolean)
        fun loadMore(isNetworkConnected: Boolean)
        fun lastSearch(): List<Result>
    }
}
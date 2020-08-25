package com.amartinez.musicplayer.presentation.search.presenter

import com.amartinez.musicplayer.domain.model.Result
import com.amartinez.musicplayer.domain.model.Search
import com.amartinez.musicplayer.domain.usecase.SearchUseCase
import com.amartinez.musicplayer.domain.usecase.local.SaveSearchUseCase
import com.amartinez.musicplayer.domain.usecase.local.SearchLocalUseCase
import com.amartinez.musicplayer.presentation.search.SearchContract
import io.reactivex.observers.DisposableObserver

class SearchPresenter(private val searchUseCase: SearchUseCase,
                      private val saveSearchUseCase: SaveSearchUseCase,
                      private val searchLocalUseCase: SearchLocalUseCase) : SearchContract.Presenter {

    private lateinit var view: SearchContract.View
    private val limit = 20
    private lateinit var term: String
    private var page: Int = 1
    private lateinit var searchResult: Search

    companion object {
        private lateinit var instance: SearchPresenter

        fun getInstance(searchUseCase: SearchUseCase, saveSearchUseCase: SaveSearchUseCase,
                        searchLocalUseCase: SearchLocalUseCase): SearchPresenter {

            if(!this::instance.isInitialized)
                instance = SearchPresenter(searchUseCase, saveSearchUseCase, searchLocalUseCase)

            return instance
        }
    }

    override fun initialize(view: SearchContract.View) {
        this.view = view
    }

    override fun search(term: String, isNetworkConnected: Boolean) {
        this.term = term
        page = 1
        if (isNetworkConnected) {
            searchUseCase.setData(term, limit)
                .execute(object : DisposableObserver<Search>() {
                    override fun onNext(value: Search) {
                        value.term = term
                        searchResult = value
                        view.displaySearchResults(value.results)
                        saveSearch(value)
                    }

                    override fun onError(e: Throwable) {
                        view.showError()
                    }

                    override fun onComplete() {}
                })
        } else {
            searchLocalUseCase.setData(term).execute(object : DisposableObserver<Search>() {
                override fun onNext(value: Search) {
                    searchResult = value
                    view.displaySearchResults(value.results)
                }

                override fun onError(e: Throwable) {
                    view.showError()
                }

                override fun onComplete() {}
            })
        }
    }

    override fun loadMore(isNetworkConnected: Boolean) {
        page++
        if (isNetworkConnected) {
            searchUseCase.setData(term, limit*page)
                .execute(object : DisposableObserver<Search>() {
                    override fun onNext(value: Search) {
                        value.term = term
                        for(i in 0 until value.results.size - limit - 1) {
                            (value.results as ArrayList).removeAt(i)
                        }

                        searchResult.results.addAll(value.results)
                        view.displaySearchResults(value.results)
                        saveSearch(value)
                    }

                    override fun onError(e: Throwable) {
                        view.showError()
                    }

                    override fun onComplete() {}
                })
        } else {
            searchLocalUseCase.setData(term).execute(object : DisposableObserver<Search>() {
                override fun onNext(value: Search) {
                    view.displaySearchResults(value.results)
                }

                override fun onError(e: Throwable) {
                    val searchError = Search()
                    view.showError()
                }

                override fun onComplete() {}
            })
        }
    }

    override fun lastSearch(): List<Result> {
        if(this::searchResult.isInitialized)
            return searchResult.results

        return ArrayList()
    }

    private fun saveSearch(search: Search) {
        saveSearchUseCase.setData(search).execute(object : DisposableObserver<Boolean>() {
            override fun onComplete() {}

            override fun onNext(t: Boolean) {}

            override fun onError(e: Throwable) {}
        })
    }
}

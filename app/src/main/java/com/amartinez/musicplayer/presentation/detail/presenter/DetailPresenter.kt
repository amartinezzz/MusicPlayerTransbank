package com.amartinez.musicplayer.presentation.detail.presenter

import com.amartinez.musicplayer.domain.model.Search
import com.amartinez.musicplayer.domain.usecase.LoadAlbumUseCase
import com.amartinez.musicplayer.presentation.detail.DetailContract
import io.reactivex.observers.DisposableObserver

class DetailPresenter(private val loadAlbumUseCase: LoadAlbumUseCase) : DetailContract.Presenter {

    private lateinit var view: DetailContract.View

    companion object {
        private lateinit var instance: DetailPresenter

        fun getInstance(loadAlbumUseCase: LoadAlbumUseCase): DetailPresenter {

            if(!this::instance.isInitialized)
                instance = DetailPresenter(loadAlbumUseCase)

            return instance
        }
    }

    override fun initialize(view: DetailContract.View) {
        this.view = view
    }

    override fun loadAlbum(id: Long, isNetworkConnected: Boolean) {
        if (isNetworkConnected) {
            loadAlbumUseCase.setData(id)
                .execute(object : DisposableObserver<Search>() {
                    override fun onNext(value: Search) {
                        view.displaySongs(value.results)
                    }

                    override fun onError(e: Throwable) {
                        view.showError()
                    }

                    override fun onComplete() {}
                })
        }
    }
}

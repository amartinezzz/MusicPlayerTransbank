package com.amartinez.musicplayer.domain.usecase

import com.amartinez.musicplayer.data.repository.Repository
import com.amartinez.musicplayer.domain.model.Search
import dagger.internal.Preconditions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoadAlbumUseCase @Inject constructor(private val repository: Repository) {

    private var id: Long = 0
    private val entity = "song"

    private fun createObservableUseCase(): Observable<Search> {
        return repository.loadAlbum(id, entity)
    }

    fun setData(id: Long): LoadAlbumUseCase {
        this.id = id
        return this
    }

    fun execute(disposableObserver: DisposableObserver<Search>) {
        Preconditions.checkNotNull<Any>(disposableObserver)
        val observable: Observable<Search> = createObservableUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        val observer: DisposableObserver<Search> = observable.subscribeWith(disposableObserver)
        CompositeDisposable().add(observer)
    }
}
package com.amartinez.musicplayer.domain.usecase.local

import com.amartinez.musicplayer.data.repository.LocalRepository
import com.amartinez.musicplayer.data.repository.mapper.local.SearchLocalToDomainMapper
import com.amartinez.musicplayer.domain.model.Search
import dagger.internal.Preconditions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchLocalUseCase @Inject constructor(
    private val repository: LocalRepository,
    private val mapper: SearchLocalToDomainMapper
) {
    private var data: String = ""

    private fun createObservableUseCase(): Observable<Search> {
        return repository.search(data, 1, 20).map{
                searchLocal -> mapper.map(searchLocal)
        }
    }

    fun setData(data: String): SearchLocalUseCase {
        this.data = data
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
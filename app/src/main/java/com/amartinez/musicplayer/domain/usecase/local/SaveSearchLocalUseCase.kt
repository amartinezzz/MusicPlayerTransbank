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

class SaveSearchUseCase @Inject constructor(
    private val mapper: SearchLocalToDomainMapper, 
    private val repository: LocalRepository
) {
    private var search: Search = Search()

    fun setData(search: Search): SaveSearchUseCase {
        this.search = search
        return this
    }

    private fun createObservableUseCase(): Observable<Boolean> {
        return repository.save(mapper.reverseMap(search))
    }

    fun execute(disposableObserver: DisposableObserver<Boolean>) {
        Preconditions.checkNotNull<Any>(disposableObserver)
        val observable: Observable<Boolean> = createObservableUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        val observer: DisposableObserver<Boolean> = observable.subscribeWith(disposableObserver)
        CompositeDisposable().add(observer)
    }
}
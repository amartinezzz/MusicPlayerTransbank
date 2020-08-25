package com.amartinez.musicplayer.data.repository

import com.amartinez.musicplayer.data.local.SearchLocal
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.realm.Realm
import javax.inject.Inject

class LocalRepository @Inject constructor() {
    fun search(term: String, start: Int, end: Int): Observable<SearchLocal> {
        return Observable.create<SearchLocal> { e ->
            val realm = Realm.getDefaultInstance()
            val searchLocal: SearchLocal? = realm.where<SearchLocal>(SearchLocal::class.java)
                .equalTo("term", term).findFirst()
            if (searchLocal != null) e.onNext(searchLocal) else e.onError(Throwable("Search not found"))
            realm.close()
            e.onComplete()
        }
    }

    fun save(item: SearchLocal): Observable<Boolean> {
        return Observable.create { e ->
            val realm = Realm.getDefaultInstance()
            try {
                if (realm.isInTransaction) realm.cancelTransaction()
            } catch (ex: Exception) {
            }
            realm.beginTransaction()
            realm.insertOrUpdate(item)
            realm.commitTransaction()
            e.onNext(true)
            e.onComplete()
            realm.close()
        }
    }
}
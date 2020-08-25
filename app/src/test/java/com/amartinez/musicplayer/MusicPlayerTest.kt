package com.amartinez.musicplayer

import android.content.Context
import com.amartinez.musicplayer.data.entity.ResultEntity
import com.amartinez.musicplayer.data.local.ResultLocal
import com.amartinez.musicplayer.data.local.SearchLocal
import com.amartinez.musicplayer.data.remote.Api
import com.amartinez.musicplayer.data.remote.response.SearchResponse
import com.amartinez.musicplayer.data.repository.RepositoryImp
import com.amartinez.musicplayer.data.repository.mapper.entity.ResultEntityToDomainMapper
import com.amartinez.musicplayer.data.repository.mapper.entity.SearchResponseToDomainMapper
import com.amartinez.musicplayer.data.repository.mapper.local.ResultLocalToDomainMapper
import com.amartinez.musicplayer.data.repository.mapper.local.SearchLocalToDomainMapper
import com.amartinez.musicplayer.domain.model.Result
import com.amartinez.musicplayer.domain.model.Search
import com.amartinez.musicplayer.domain.usecase.SearchUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class MusicPlayerTest {

    @Mock
    private lateinit var context: Context

    @Test
    fun searchWithObservableNoMapping() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val api = retrofit.create(Api::class.java)

        val disposable = object : DisposableObserver<SearchResponse>() {
            override fun onNext(value: SearchResponse) {
                Assert.assertTrue(value.results.isNotEmpty())
            }

            override fun onError(e: Throwable) {
                Assert.assertTrue(false)
            }

            override fun onComplete() {}
        }

        val observable = api.search("shakira", "music", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        val observer: DisposableObserver<SearchResponse> = observable.subscribeWith(disposable)
        CompositeDisposable().add(observer)
    }

    @Test
    fun searchWithObservableWithMapping() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val api = retrofit.create(Api::class.java)

        val disposable = object : DisposableObserver<Search>() {
            override fun onNext(value: Search) {
                Assert.assertTrue(value.results.isNotEmpty())
            }

            override fun onError(e: Throwable) {
                Assert.assertTrue(false)
            }

            override fun onComplete() {}
        }

        val mapper = SearchResponseToDomainMapper(ResultEntityToDomainMapper())
        val observable = api.search("shakira", "music", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                mapper.map(it)
            }

        val observer: DisposableObserver<Search> = observable.subscribeWith(disposable)
        CompositeDisposable().add(observer)
    }

    private fun ResultEntity.toDomain() = Result(
        kind, artistId, collectionId, trackId, artistName, collectionName,
        trackName, collectionCensoredName, trackCensoredName, artistViewUrl, collectionViewUrl,
        trackViewUrl)

    private fun SearchResponse.toDomain() = Search (
        "", resultCount, results.map {
            it.toDomain()
        }
    )

    @Test
    fun searchWithObservableWithMapping2() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val api = retrofit.create(Api::class.java)

        val disposable = object : DisposableObserver<Search>() {
            override fun onNext(value: Search) {
                Assert.assertTrue(value.results.isNotEmpty())
            }

            override fun onError(e: Throwable) {
                Assert.assertTrue(false)
            }

            override fun onComplete() {}
        }

        val observable = api.search("shakira", "music", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.toDomain()
            }

        val observer: DisposableObserver<Search> = observable.subscribeWith(disposable)
        CompositeDisposable().add(observer)
    }

    @Test
    fun searchWithObservableWithUseCase() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)

        val usecase = SearchUseCase(RepositoryImp(api, SearchResponseToDomainMapper(ResultEntityToDomainMapper())))
        usecase.setData("shakira", 20).execute(object : DisposableObserver<Search>() {
            override fun onNext(value: Search) {
                Assert.assertTrue(value.results.isNotEmpty())
            }

            override fun onError(e: Throwable) {
                Assert.assertTrue(false)
            }

            override fun onComplete() {}
        })
    }

    private fun initializeRealm() : Realm {
        context = mock<Context>(Context::class.java)
        Realm.init(context)
        val testConfig = RealmConfiguration.Builder().inMemory().name("test-realm").build()

        return Realm.getInstance(testConfig)
    }

    @Test
    fun testLocalSearch() {
        val term = "shakira"
        val list = RealmList<ResultLocal>()
        list.add(ResultLocal())
        list.add(ResultLocal())
        list.add(ResultLocal())

        val item = SearchLocal(term, list.size.toLong(), list)

        val realm = initializeRealm()
        realm.deleteAll()
        try {
            if (realm.isInTransaction) realm.cancelTransaction()
        } catch (ex: Exception) {
        }
        realm.beginTransaction()
        realm.insertOrUpdate(item)
        realm.commitTransaction()

        val searchLocal: SearchLocal? = realm.where<SearchLocal>(SearchLocal::class.java)
            .equalTo("term", term).findFirst()
        if (searchLocal != null) {
            Assert.assertTrue(true)
        } else {
            Assert.assertTrue(false)
        }
        realm.close()
    }

    @Test
    fun testLocalSearchWithRemoteDataMapping1() {
        val term = "shakira"
        val mapper = SearchLocalToDomainMapper(ResultLocalToDomainMapper())

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)

        val usecase = SearchUseCase(RepositoryImp(api, SearchResponseToDomainMapper(ResultEntityToDomainMapper())))
        usecase.setData(term, 20).execute(object : DisposableObserver<Search>() {
            override fun onNext(value: Search) {
                val item = mapper.reverseMap(value)
                val realm = initializeRealm()
                realm.deleteAll()
                try {
                    if (realm.isInTransaction) realm.cancelTransaction()
                } catch (ex: Exception) {
                }
                realm.beginTransaction()
                realm.insertOrUpdate(item)
                realm.commitTransaction()

                val searchLocal: SearchLocal? = realm.where<SearchLocal>(SearchLocal::class.java)
                    .equalTo("term", term).findFirst()
                if (searchLocal != null) {
                    Assert.assertTrue(true)
                } else {
                    Assert.assertTrue(false)
                }
                realm.close()
            }

            override fun onError(e: Throwable) {
                Assert.assertTrue(false)
            }

            override fun onComplete() {}
        })
    }


    private fun listResultToRealmList(list: List<Result>) : RealmList<ResultLocal> {
        val realmList = RealmList<ResultLocal>()
        realmList.addAll(list.map { it.toLocal() })

        return realmList
    }

    private fun Result.toLocal() = ResultLocal(
        1, kind, artistId, collectionId, trackId, artistName, collectionName, trackName,
        artistViewUrl, trackViewUrl, previewUrl, artworkUrl30, artworkUrl60, artworkUrl100,
        isStreamable)

    private fun Search.toLocal() = SearchLocal(
        term, resultCount, listResultToRealmList(results)
    )

    @Test
    fun testLocalSearchWithRemoteDataMapping2() {
        val term = "shakira"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)

        val usecase = SearchUseCase(RepositoryImp(api, SearchResponseToDomainMapper(ResultEntityToDomainMapper())))
        usecase.setData(term, 20).execute(object : DisposableObserver<Search>() {
            override fun onNext(value: Search) {
                val item = value.toLocal()
                val realm = initializeRealm()
                realm.deleteAll()
                try {
                    if (realm.isInTransaction) realm.cancelTransaction()
                } catch (ex: Exception) {
                }
                realm.beginTransaction()
                realm.insertOrUpdate(item)
                realm.commitTransaction()

                val searchLocal: SearchLocal? = realm.where<SearchLocal>(SearchLocal::class.java)
                    .equalTo("term", term).findFirst()
                if (searchLocal != null) {
                    Assert.assertTrue(true)
                } else {
                    Assert.assertTrue(false)
                }
                realm.close()
            }

            override fun onError(e: Throwable) {
                Assert.assertTrue(false)
            }

            override fun onComplete() {}
        })
    }

    @Test
    fun testMappers() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val api = retrofit.create(Api::class.java)

        val mapper = SearchResponseToDomainMapper(ResultEntityToDomainMapper())
        val disposable = object : DisposableObserver<SearchResponse>() {
            override fun onNext(value: SearchResponse) {
                //Assert.assertTrue(value.results.isNotEmpty())

                var initialTime = Calendar.getInstance().time.time
                var item = mapper.map(value)
                var endTime = Calendar.getInstance().time.time
                var totalMapper1 = endTime-initialTime
                println("tiempo: $totalMapper1")

                initialTime = Calendar.getInstance().time.time
                item = mapper.map(value)
                endTime = Calendar.getInstance().time.time
                val totalMapper2 = endTime-initialTime
                println("tiempo 2: $totalMapper2")

                Assert.assertTrue(totalMapper1 > totalMapper2)
            }

            override fun onError(e: Throwable) {
                Assert.assertTrue(false)
            }

            override fun onComplete() {}
        }

        val observable = api.search("shakira", "music", 20)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        val observer: DisposableObserver<SearchResponse> = observable.subscribeWith(disposable)
        CompositeDisposable().add(observer)
    }
}
package com.amartinez.musicplayer.di

import com.amartinez.musicplayer.BuildConfig
import com.amartinez.musicplayer.data.remote.Api
import com.amartinez.musicplayer.data.repository.Repository
import com.amartinez.musicplayer.data.repository.RepositoryImp
import com.amartinez.musicplayer.data.repository.mapper.entity.SearchResponseToDomainMapper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class ProviderModule {

    @Provides
    fun provideRepository(api: Api, mapper: SearchResponseToDomainMapper): Repository {
        return RepositoryImp(api, mapper)
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideApiService(): Api {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(Api::class.java)
    }
}
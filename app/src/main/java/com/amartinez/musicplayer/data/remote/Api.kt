package com.amartinez.musicplayer.data.remote

import com.amartinez.musicplayer.data.remote.response.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @Headers("Accept: application/json")
    @GET("search")
    fun search(
        @Query("term") term: String?,
        @Query("mediaType") mediaType: String?,
        @Query("limit") limit: Int
    ): Observable<SearchResponse>
}
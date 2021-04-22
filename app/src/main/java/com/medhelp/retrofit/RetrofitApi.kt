package com.medhelp.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.QueryMap
import retrofit2.http.GET


interface RetrofitApi {
    @GET("/v2/sources")
    fun getSources(@QueryMap query: Map<String?, String?>?): Call<JsonObject?>?

    @GET("/v2/top-headlines")
    fun getTopHeadlines(@QueryMap query: Map<String?, String?>?): Call<JsonObject?>?

    @GET("/v2/everything")
    fun getEverything(@QueryMap query: Map<String?, String?>?): Call<JsonObject?>?
}
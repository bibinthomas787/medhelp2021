package com.medhelp.retrofit

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit




class RetrofitClient {
    private var mRetrofit: Retrofit? = null

    private fun getRetrofit(): Retrofit? {
        if (mRetrofit == null) {
            mRetrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return mRetrofit
    }

    fun getAPIService(): RetrofitApi? {
        return getRetrofit()!!.create(RetrofitApi::class.java)
    }

}
package com.example.dictionmaster.data.remote

import com.example.dictionmaster.data.models.APIModelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceAPI {

    @GET("entries/{source_lang}/{word_id}")
    suspend fun getData(
        @Path("source_lang") language: String,
        @Path("word_id") word: String,
        @Query("strictMatch") strictMatch: Boolean
    ): Response<APIModelResponse>

}
package com.example.dictionmaster.service.repository

import com.example.dictionmaster.service.remote.ServiceAPI
import javax.inject.Inject

class DictionRepository @Inject constructor(
    private val api: ServiceAPI,

    ) {

    suspend fun getData(
        language: String,
        word: String,
        strictMatch: Boolean = false
    ) = api.getData(language, word, strictMatch)
}
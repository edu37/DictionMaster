package com.example.dictionmaster.data.repository

import com.example.dictionmaster.data.models.APIModelResponse
import com.example.dictionmaster.data.models.RequestModel
import com.example.dictionmaster.data.models.WordModel
import com.example.dictionmaster.data.remote.ServiceAPI
import com.example.dictionmaster.util.Constants.USERS
import com.example.dictionmaster.util.Constants.WORDS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DictionRepository @Inject constructor(
    private val api: ServiceAPI,
) {

    private val db by lazy { Firebase.firestore }
    private val auth by lazy { Firebase.auth }

    suspend fun getData(
        language: String,
        word: String,
        strictMatch: Boolean = false
    ) = api.getData(language, word, strictMatch)

    suspend fun saveResponse(response: APIModelResponse) = withContext(Dispatchers.IO) {
//        val id = response.results?.get(0)?.id?.lowercase()
//        val audiofile =
//            response.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(0)?.audioFile
//        val pronunciation =
//            response.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(0)?.phoneticSpelling
//        val examples =
//            response.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.examples
//        val definition =
//            response.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.definitions
//
//        val example: MutableList<String> = arrayListOf()
//        examples?.forEach { example.add(it.text.toString()) }
//        val wordModel = WordModel(id, audiofile, pronunciation, definition, example)
//        db.collection(USERS).document(auth.uid!!).collection(WORDS).document(id!!).set(wordModel)
        db.collection(USERS).document(auth.uid!!).update("request", FieldValue.increment(1))
    }



    suspend fun saveUser() = withContext(Dispatchers.IO) {
        //db.collection(USERS).document(auth.uid!!).collection(WORDS).add(WordModel())
        db.collection(USERS).document(auth.uid!!).set(RequestModel())
    }



}
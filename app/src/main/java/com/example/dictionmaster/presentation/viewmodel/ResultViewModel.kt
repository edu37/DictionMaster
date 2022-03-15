package com.example.dictionmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionmaster.data.models.*
import com.example.dictionmaster.data.repository.DictionRepository
import com.example.dictionmaster.presentation.state.ResourceState
import com.example.dictionmaster.util.Constants
import com.example.dictionmaster.util.Constants.USERS
import com.example.dictionmaster.util.Constants.WORDS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: DictionRepository
) : ViewModel() {

    private val db by lazy { Firebase.firestore }
    private val auth by lazy { Firebase.auth }

    private val mData =
        MutableStateFlow<ResourceState<APIModelResponse>>(ResourceState.Empty())
    val data: StateFlow<ResourceState<APIModelResponse>> = mData

    fun fetch(language: String, word: String) = viewModelScope.launch {
        mData.value = ResourceState.Loading()
        delay(1000L)
        safeFetch(language, word)
    }

    private fun safeFetch(language: String, word: String) {
        try {
            verifyRequests(language, word)
        } catch (e: Exception) {
            mData.value = ResourceState.Error(e.message)
        }
    }

    private fun verifyRequests(language: String, word: String) {
        db.collection(USERS).addSnapshotListener { documentos, _ ->
            if (documentos != null) {
                for (documento in documentos.documentChanges) {
                    val request = documento.document.toObject(RequestModel::class.java)
                    if (request.request < 10) {
                        viewModelScope.launch {
                            verifyWord(language, word)
                        }
                    }
                    else{
                        mData.value = ResourceState.Error(Constants.PURCHASE)
                    }
                }
            }
        }
    }

    private suspend fun verifyWord(language: String, word: String) {
        db.collection(USERS).document(auth.uid!!)
            .collection(WORDS).addSnapshotListener { documentos, _ ->
                if (documentos != null) {
                    for (documento in documentos.documentChanges) {
                        val wordModel = documento.document.toObject(WordModel::class.java)
                        if (wordModel.word == word) {
                            mData.value = ResourceState.Error(Constants.GET_FROM_DATABASE)
                        } else {
                            viewModelScope.launch {
                                getResponse(language, word)
                            }
                        }
                    }
                }
            }
    }

    private suspend fun getResponse(language: String, word: String) {
        val response = repository.getData(language, word)
        mData.value = handleResponse(response)
    }

    private suspend fun handleResponse(response: Response<APIModelResponse>): ResourceState<APIModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                repository.saveResponse(it)
                return ResourceState.Success(it)
            }
        }
        return ResourceState.Error(response.message())
    }

}

package com.example.dictionmaster.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionmaster.service.models.*
import com.example.dictionmaster.service.repository.DictionRepository
import com.example.dictionmaster.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: DictionRepository
) : ViewModel() {

    private val mData =
        MutableStateFlow<ResourceState<APIModelResponse>>(ResourceState.Empty())
    val data: StateFlow<ResourceState<APIModelResponse>> = mData

    private val mExamples = MutableStateFlow<ResourceState<List<Examples>>>(ResourceState.Loading())
    val examples: StateFlow<ResourceState<List<Examples>>> = mExamples

    private val mDefinition = MutableStateFlow<ResourceState<List<Sense>>>(ResourceState.Loading())
    val definition: StateFlow<ResourceState<List<Sense>>> = mDefinition

    private val mPronunciation =
        MutableStateFlow<ResourceState<List<Pronunciations>>>(ResourceState.Loading())
    val pronunciation: StateFlow<ResourceState<List<Pronunciations>>> = mPronunciation

    fun fetch(language: String, word: String) = viewModelScope.launch {
        safeFetch(language, word)
    }

    private suspend fun safeFetch(language: String, word: String) {
        try {
//            if (responseDefinition.isSuccessful) {
//                mDefinition.value =
//                    handleDefinitionResponse(responseDefinition.body()!!.results[0].lexicalEntries[0].entries[0].senses)
//            }
//            if (responseExample.isSuccessful) {
//                mExamples.value =
//                    handleExampleResponse(responseExample.body()!!.results[0].lexicalEntries[0].entries[0].senses[0].examples)
//            }
//            if (responsePronunciation.isSuccessful) {
//                mPronunciation.value =
//                    handlePronunciationResponse(responsePronunciation.body()!!.results[0].lexicalEntries[0].entries[0].pronunciations)
//            }
            //mData.value = handleResponse(response)
            val response = repository.getData(language, word)
            mData.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    mData.value =
                        ResourceState.Error("Erro de conexão com a internet")
                }
                else -> mData.value = ResourceState.Error("Falha na conversão de dados")
            }
        }
    }

    private fun handlePronunciationResponse(pronunciations: List<Pronunciations>): ResourceState<List<Pronunciations>> {
        return ResourceState.Success(pronunciations)
    }

    private fun handleDefinitionResponse(sense: List<Sense>): ResourceState<List<Sense>> {
        return ResourceState.Success(sense)
    }

    private fun handleExampleResponse(examples: List<Examples>): ResourceState<List<Examples>> {
        return ResourceState.Success(examples)
    }

    private fun handleResponse(response: Response<APIModelResponse>): ResourceState<APIModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return ResourceState.Success(it)
            }
        }
        return ResourceState.Error(response.message())
    }

}

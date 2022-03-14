package com.example.dictionmaster.ui.viewmodel

import android.util.Log
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

    private fun handleResponse(response: Response<APIModelResponse>): ResourceState<APIModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return ResourceState.Success(it)
            }
        }
        return ResourceState.Error(response.message())
    }

}

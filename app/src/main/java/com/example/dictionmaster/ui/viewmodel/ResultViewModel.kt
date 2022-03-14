package com.example.dictionmaster.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionmaster.service.models.*
import com.example.dictionmaster.service.repository.DictionRepository
import com.example.dictionmaster.ui.state.ResourceState
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

    private val mData =
        MutableStateFlow<ResourceState<APIModelResponse>>(ResourceState.Empty())
    val data: StateFlow<ResourceState<APIModelResponse>> = mData

    fun fetch(language: String, word: String) = viewModelScope.launch {
        mData.value = ResourceState.Loading()
        delay(1000L)
        safeFetch(language, word)
    }

    private suspend fun safeFetch(language: String, word: String) {
        try {
            val response = repository.getData(language, word)
            mData.value = handleResponse(response)
        } catch (e: Exception) {
            mData.value = ResourceState.Error(e.message)
        }
    }


    private suspend fun handleResponse(response: Response<APIModelResponse>): ResourceState<APIModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return ResourceState.Success(it)
            }
        }
        return ResourceState.Error(response.message())
    }

}

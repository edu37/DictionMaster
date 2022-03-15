package com.example.dictionmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionmaster.data.repository.DictionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: DictionRepository
): ViewModel(){

    fun saveUser() = viewModelScope.launch{
        repository.saveUser()
    }
}
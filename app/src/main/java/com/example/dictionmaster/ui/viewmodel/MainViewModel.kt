package com.example.dictionmaster.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dictionmaster.service.repository.DictionRepository
import com.example.dictionmaster.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DictionRepository
) : ViewModel() {

    private var mLanguage = MutableStateFlow<String>("en-gb")
    val language: StateFlow<String> = mLanguage

    fun updateLanguage(language: String) {
        when (language) {
            Constants.ENGLISH -> {
                mLanguage.value = "en-gb"
            }
            Constants.SPANISH -> {
                mLanguage.value = "es"
            }
            else -> {
                mLanguage.value = "fr"
            }
        }
    }

}
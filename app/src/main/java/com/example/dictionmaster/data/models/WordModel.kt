package com.example.dictionmaster.data.models

data class WordModel(
    var word: String? = "",
    var audioFile: String? = "",
    var pronunciation: String? = "",
    var definitions: List<String>? = emptyList(),
    var examples: List<String>? = emptyList()
)

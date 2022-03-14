package com.example.dictionmaster.service.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LexicalEntry(

    @SerializedName("entries")
    val entries: List<Entry>
) : Serializable

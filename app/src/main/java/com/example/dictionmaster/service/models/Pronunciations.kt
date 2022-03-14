package com.example.dictionmaster.service.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pronunciations(

    @SerializedName("audioFile")
    val audioFile: String,

    @SerializedName("phoneticSpelling")
    val phoneticSpelling: String

) : Serializable

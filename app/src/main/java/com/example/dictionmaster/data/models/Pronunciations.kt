package com.example.dictionmaster.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pronunciations(

    @SerializedName("audioFile")
    val audioFile: String?,

    @SerializedName("phoneticSpelling")
    val phoneticSpelling: String?

) : Serializable

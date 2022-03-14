package com.example.dictionmaster.service.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Entry(
    @SerializedName("pronunciations")
    val pronunciations: List<Pronunciations>,

    @SerializedName("senses")
    val senses: List<Sense>

) : Serializable

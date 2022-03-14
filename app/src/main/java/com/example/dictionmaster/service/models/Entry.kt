package com.example.dictionmaster.service.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Entry(
    @SerializedName("pronunciations")
    val pronunciations: MutableList<Pronunciations>?,

    @SerializedName("senses")
    val senses: MutableList<Sense>?

) : Serializable

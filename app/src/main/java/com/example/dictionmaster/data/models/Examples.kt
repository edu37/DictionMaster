package com.example.dictionmaster.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Examples(

    @SerializedName("text")
    val text: String?
) : Serializable

package com.example.dictionmaster.service.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Definitions(

    @SerializedName("string")
    val text: String
):Serializable
package com.example.dictionmaster.service.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sense(

    @SerializedName("definitions")
    val definitions: List<Definitions>,

    @SerializedName("examples")
    val examples: List<Examples>

):Serializable

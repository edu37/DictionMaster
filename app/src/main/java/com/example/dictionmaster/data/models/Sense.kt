package com.example.dictionmaster.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sense(

    @SerializedName("definitions")
    val definitions: MutableList<String>?,

    @SerializedName("examples")
    val examples: MutableList<Examples>?

):Serializable

package com.example.dictionmaster.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class APIModelResponse(

    @SerializedName("results")
    val results: MutableList<HeadwordEntryModel>?
):Serializable

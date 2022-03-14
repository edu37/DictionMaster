package com.example.dictionmaster.service.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class APIModelResponse(

    @SerializedName("results")
    val results: List<HeadwordEntryModel>
):Serializable

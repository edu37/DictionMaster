package com.example.dictionmaster.data.models

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class HeadwordEntryModel(
    @SerializedName("id")
    val id: String,

    @SerializedName("lexicalEntries")
    val lexicalEntries: MutableList<LexicalEntry>?,
) : Serializable

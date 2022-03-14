package com.example.dictionmaster.service.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "dictionModel")
data class HeadwordEntryModel(

    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("lexicalEntries")
    val lexicalEntries: List<LexicalEntry>,
) : Serializable

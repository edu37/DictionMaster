package com.example.dictionmaster.data.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "lexicalEntryModel")
data class Entry(
    @SerializedName("pronunciations")
    val pronunciations: MutableList<Pronunciations>?,

    @SerializedName("senses")
    val senses: MutableList<Sense>?

) : Serializable

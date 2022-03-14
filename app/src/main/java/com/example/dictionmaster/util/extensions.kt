package com.example.dictionmaster.util

fun <E> MutableList<E>.cleanList(): List<E> {
    return this.filterNotNull()
}
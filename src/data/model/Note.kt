package com.pasukanlangit.data.model

data class Note(
    val id: Int,
    val userEmail: String,
    val title: String,
    val description: String,
    val date: Long
)

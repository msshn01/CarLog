package com.example.carlog.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Maintenance(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val date: String = ""
)
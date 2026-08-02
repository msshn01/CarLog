package com.example.carlog.model

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.UUID

@IgnoreExtraProperties
data class Car(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val model: String? = null,
    val year: String? = null,
    val km: String = "0",
    val maintenanceList: MutableList<Maintenance> = mutableListOf()
)
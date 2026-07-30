package com.example.carlog.model

import java.util.UUID

data class Car(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val model: String? = null,
    val year: String? = null,
    val km: String = "0",
    val maintenanceList: MutableList<String> = mutableListOf()
)
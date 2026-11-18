package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_reports")
data class IncidentReport(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // "Harassment", "Assault", "Following", "Workplace", "Cybercrime", "Child Safety"
    val timestamp: Long = System.currentTimeMillis(),
    val locationDescription: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val description: String,
    val perpetratorDetails: String = "",
    val evidenceNotes: String = "",
    val mediaFilePath: String? = null,
    val isPreservedOffline: Boolean = true
)

package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.IncidentReport
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentReportDao {
    @Query("SELECT * FROM incident_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<IncidentReport>>

    @Query("SELECT * FROM incident_reports WHERE category = :category ORDER BY timestamp DESC")
    fun getReportsByCategory(category: String): Flow<List<IncidentReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: IncidentReport): Long

    @Delete
    suspend fun deleteReport(report: IncidentReport)

    @Query("DELETE FROM incident_reports")
    suspend fun clearAllReports()
}

package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ContactDao
import com.example.data.dao.IncidentReportDao
import com.example.data.entity.IncidentReport
import com.example.data.entity.TrustedContact

@Database(
    entities = [TrustedContact::class, IncidentReport::class],
    version = 1,
    exportSchema = false
)
abstract class RakshaDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun incidentReportDao(): IncidentReportDao

    companion object {
        @Volatile
        private var INSTANCE: RakshaDatabase? = null

        fun getDatabase(context: Context): RakshaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RakshaDatabase::class.java,
                    "raksha_kavach_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BloodDonation
import com.example.data.model.BloodRequest
import com.example.data.model.EmergencyContact
import com.example.data.model.MedicalRecord
import com.example.data.model.NotificationItem
import com.example.data.model.UserProfile

@Database(
  entities = [
    UserProfile::class,
    MedicalRecord::class,
    EmergencyContact::class,
    BloodDonation::class,
    BloodRequest::class,
    NotificationItem::class
  ],
  version = 1,
  exportSchema = false
)
abstract class JeevixDatabase : RoomDatabase() {
  abstract fun userProfileDao(): UserProfileDao
  abstract fun medicalRecordDao(): MedicalRecordDao
  abstract fun emergencyContactDao(): EmergencyContactDao
  abstract fun bloodDonationDao(): BloodDonationDao
  abstract fun bloodRequestDao(): BloodRequestDao
  abstract fun notificationDao(): NotificationDao

  companion object {
    @Volatile
    private var INSTANCE: JeevixDatabase? = null

    fun getDatabase(context: Context): JeevixDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          JeevixDatabase::class.java,
          "jeevix_health_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}

package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BloodDonation
import com.example.data.model.BloodRequest
import com.example.data.model.EmergencyContact
import com.example.data.model.MedicalRecord
import com.example.data.model.NotificationItem
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getUserProfile(): Flow<UserProfile?>

  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  suspend fun getUserProfileOnce(): UserProfile?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateProfile(profile: UserProfile)

  @Update
  suspend fun updateProfile(profile: UserProfile)
}

@Dao
interface MedicalRecordDao {
  @Query("SELECT * FROM medical_records ORDER BY yearOrDate DESC, id DESC")
  fun getAllRecords(): Flow<List<MedicalRecord>>

  @Query("SELECT * FROM medical_records WHERE type = :type ORDER BY yearOrDate DESC")
  fun getRecordsByType(type: String): Flow<List<MedicalRecord>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRecord(record: MedicalRecord): Long

  @Delete
  suspend fun deleteRecord(record: MedicalRecord)

  @Query("DELETE FROM medical_records WHERE id = :id")
  suspend fun deleteRecordById(id: Long)
}

@Dao
interface EmergencyContactDao {
  @Query("SELECT * FROM emergency_contacts ORDER BY isPrimary DESC, id ASC")
  fun getAllContacts(): Flow<List<EmergencyContact>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertContact(contact: EmergencyContact): Long

  @Update
  suspend fun updateContact(contact: EmergencyContact)

  @Delete
  suspend fun deleteContact(contact: EmergencyContact)

  @Query("DELETE FROM emergency_contacts WHERE id = :id")
  suspend fun deleteContactById(id: Long)
}

@Dao
interface BloodDonationDao {
  @Query("SELECT * FROM blood_donations ORDER BY id DESC")
  fun getAllDonations(): Flow<List<BloodDonation>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDonation(donation: BloodDonation): Long

  @Delete
  suspend fun deleteDonation(donation: BloodDonation)
}

@Dao
interface BloodRequestDao {
  @Query("SELECT * FROM blood_requests ORDER BY CASE WHEN urgency = 'Emergency' THEN 1 WHEN urgency = 'Urgent' THEN 2 ELSE 3 END, id DESC")
  fun getAllRequests(): Flow<List<BloodRequest>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRequest(request: BloodRequest): Long

  @Update
  suspend fun updateRequest(request: BloodRequest)

  @Query("UPDATE blood_requests SET isFulfilled = :fulfilled WHERE id = :id")
  suspend fun updateFulfilled(id: Long, fulfilled: Boolean)
}

@Dao
interface NotificationDao {
  @Query("SELECT * FROM notifications ORDER BY id DESC")
  fun getAllNotifications(): Flow<List<NotificationItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotification(notification: NotificationItem): Long

  @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
  suspend fun markAsRead(id: Long)

  @Query("DELETE FROM notifications")
  suspend fun clearAll()
}

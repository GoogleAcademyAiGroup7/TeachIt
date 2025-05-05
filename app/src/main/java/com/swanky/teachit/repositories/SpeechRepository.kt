package com.swanky.teachit.repositories

import androidx.lifecycle.LiveData
import com.swanky.teachit.db.Dao
import com.swanky.teachit.db.entity.SpeechRecord

class SpeechRepository(private val speechDao: Dao) { // Constructor Dao alır

    /**
     * Veritabanındaki tüm konuşma kayıtlarını LiveData olarak sunar.
     */
    val allSpeechRecordsLiveData: LiveData<List<SpeechRecord>> = speechDao.getAllSpeechRecordsLiveData()

    /**
     * Yeni bir konuşma kaydını ekler. Hata olursa Result.failure döner.
     */
    suspend fun insertSpeechRecord(record: SpeechRecord): Result<Unit> {
        return safeCall { speechDao.insertSpeechRecord(record) }
    }

    /**
     * Belirtilen ID'ye sahip konuşma kaydını siler. Hata olursa Result.failure döner.
     */
    suspend fun deleteSpeechRecordById(recordId: Long): Result<Unit> {
        return safeCall { speechDao.deleteSpeechRecordById(recordId) }
    }

    /**
     * Tüm konuşma kayıtlarını siler. Hata olursa Result.failure döner.
     */
    suspend fun deleteAllSpeechRecords(): Result<Unit> {
        return safeCall { speechDao.deleteAllSpeechRecords() }
    }

    /**
     * DAO çağrılarını güvenli bir şekilde yapar ve sonucu Result ile sarmalar.
     */
    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: Exception) {
            // Hata loglama eklenebilir
            Result.failure(e)
        }
    }
}
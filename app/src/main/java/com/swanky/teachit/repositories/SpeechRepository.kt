package com.swanky.teachit.repositories

import androidx.lifecycle.LiveData
import com.swanky.teachit.db.Dao
import com.swanky.teachit.db.entity.SpeechRecord

class SpeechRepository(private val speechDao: Dao) { // Constructor Dao alır

    val allSpeechRecordsLiveData: LiveData<List<SpeechRecord>> = speechDao.getAllSpeechRecordsLiveData()

    suspend fun insertSpeechRecord(record: SpeechRecord): Result<Unit> {
        return safeCall { speechDao.insertSpeechRecord(record) }
    }

    suspend fun deleteSpeechRecordById(recordId: Long): Result<Unit> {
        return safeCall { speechDao.deleteSpeechRecordById(recordId) }
    }

    suspend fun deleteAllSpeechRecords(): Result<Unit> {
        return safeCall { speechDao.deleteAllSpeechRecords() }
    }

    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.swanky.teachit.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swanky.teachit.db.entity.SpeechRecord
import com.swanky.teachit.models.Evaluation
import com.swanky.teachit.models.TestResult
import com.swanky.teachit.models.Topic

@Dao
interface Dao {

    // QUERIES
    @Query("SELECT * FROM Topic")
    suspend fun getAllTopics(): List<Topic>

    @Query("SELECT * FROM Evaluation")
    suspend fun getAllEvaluations(): List<Evaluation>

    @Query("SELECT * FROM TestResult")
    suspend fun getAllTestResults(): List<TestResult>

    @Query("SELECT * FROM speech_records ORDER BY timestamp DESC")
    fun getAllSpeechRecordsLiveData(): LiveData<List<SpeechRecord>>


    // INSERT
    @Insert
    suspend fun insertTopic(topic: Topic)

    @Insert
    suspend fun insertEvaluation(evaluation: Evaluation)

    @Insert
    suspend fun insertTestResult(testResult: TestResult)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpeechRecord(record: SpeechRecord)


    // DELETE
    @Query("DELETE FROM Topic WHERE id = :topicId")
    suspend fun deleteTopic(topicId: Int)

    @Query("DELETE FROM Evaluation WHERE id = :evaluationId")
    suspend fun deleteEvaluation(evaluationId: Int)

    @Query("DELETE FROM TestResult WHERE id = :testResultId")
    suspend fun deleteTestResult(testResultId: Int)

    @Query("DELETE FROM speech_records WHERE id = :recordId")
    suspend fun deleteSpeechRecordById(recordId: Long)

    @Query("DELETE FROM speech_records")
    suspend fun deleteAllSpeechRecords()
}
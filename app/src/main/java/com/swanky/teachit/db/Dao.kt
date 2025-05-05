package com.swanky.teachit.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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


    // INSERT
    @Insert
    suspend fun insertTopic(topic: Topic)

    @Insert
    suspend fun insertEvaluation(evaluation: Evaluation)

    @Insert
    suspend fun insertTestResult(testResult: TestResult)


    // DELETE
    @Query("DELETE FROM Topic WHERE id = :topicId")
    suspend fun deleteTopic(topicId: Int)

    @Query("DELETE FROM Evaluation WHERE id = :evaluationId")
    suspend fun deleteEvaluation(evaluationId: Int)

    @Query("DELETE FROM TestResult WHERE id = :testResultId")
    suspend fun deleteTestResult(testResultId: Int)
}
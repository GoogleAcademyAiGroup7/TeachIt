package com.swanky.teachit.repositories

import com.swanky.teachit.models.Evaluation
import com.swanky.teachit.models.TestResult
import com.swanky.teachit.models.Topic
import com.swanky.teachit.db.Dao
import com.swanky.teachit.models.Achievement
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val dao: Dao) {

    // QUERIES
    suspend fun getAllTopics(): Result<List<Topic>> {
        return safeCall { dao.getAllTopics() }
    }

    suspend fun getAllEvaluations(): Result<List<Evaluation>> {
        return safeCall { dao.getAllEvaluations() }
    }

    suspend fun getAllTestResults(): Result<List<TestResult>> {
        return safeCall { dao.getAllTestResults() }
    }

    suspend fun getAllAchievements(): Result<List<Achievement>> {
        return safeCall { dao.getAllAchievements() }
    }


    // INSERT
    suspend fun insertTopic(topic: Topic): Result<Unit> {
        return safeCall { dao.insertTopic(topic) }
    }

    suspend fun insertEvaluation(evaluation: Evaluation): Result<Unit> {
        return safeCall { dao.insertEvaluation(evaluation) }
    }

    suspend fun insertTestResult(testResult: TestResult): Result<Unit> {
        return safeCall { dao.insertTestResult(testResult) }
    }


    // DELETE
    suspend fun deleteTopic(topicId: Int): Result<Unit> {
        return safeCall { dao.deleteTopic(topicId) }
    }

    suspend fun deleteEvaluation(evaluationId: Int): Result<Unit> {
        return safeCall { dao.deleteEvaluation(evaluationId) }
    }

    suspend fun deleteTestResult(testResultId: Int): Result<Unit> {
        return safeCall { dao.deleteTestResult(testResultId) }
    }


    // Helper function to handle exceptions
    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
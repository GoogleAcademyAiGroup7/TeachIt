package com.swanky.teachit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.swanky.teachit.R
import com.swanky.teachit.models.AiResponse
import com.swanky.teachit.models.Evaluation
import com.swanky.teachit.models.Topic
import com.swanky.teachit.repositories.Repository
import com.swanky.teachit.utils.ApiKeyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LearningAsistantViewmodel @Inject constructor(private val repository: Repository, application: Application) : BaseViewModel(application)  {

    private val _evaluation = MutableLiveData<Evaluation?>(null)
    val evaluation: LiveData<Evaluation?> = _evaluation

    fun sendAi(topic: Topic, userAnswer: String) {
        val generativeModel = GenerativeModel(
            // The Gemini 1.5 models are versatile and work with multi-turn conversations (like chat)
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = ApiKeyUtil.apiKey
        )

        val prompt = getApplication<Application>().getString(R.string.ai_prompt, topic, userAnswer)

        viewModelScope.launch {
            val response = generativeModel.generateContent(prompt)
            if  (response.text != null) {
                val aiResponse = parseResponse(response.text!!)
                // Convert AI response to Evaluation entity
                val parsedEvaluation = mapAIResponseToEvaluation(aiResponse, topic.id)
                _evaluation.value = parsedEvaluation

                // Save to database
                repository.insertEvaluation(parsedEvaluation)
            }
        }


    }

    // Parse the response from AI in JSON format
    private fun parseResponse(responseText: String): AiResponse {
        // Clear if there is markdown in the text
        val cleanedJson = responseText
            .replace("```json", "")
            .replace("```", "")
            .trim()

        val json = JSONObject(cleanedJson)
        val missingInfo = json.getString("missingInfo")
        val explanationQuality = json.getString("explanationQuality")
        val suggestions = json.getJSONArray("suggestions").let { suggestionsJson ->
            List(suggestionsJson.length()) { suggestionsJson.getString(it) }
        }
        val score = json.getInt("score")

        return AiResponse(missingInfo, explanationQuality, suggestions, score)
    }

    // Convert AI response to Evaluation entity
    private fun mapAIResponseToEvaluation(response: AiResponse, topicId: Int): Evaluation {
        return Evaluation(
            topicId = topicId,
            userAnswer = "",
            missingInfo = response.missingInfo,
            explanationQuality = response.explanationQuality,
            suggestions = response.suggestions,
            score = response.score
        )
    }


}
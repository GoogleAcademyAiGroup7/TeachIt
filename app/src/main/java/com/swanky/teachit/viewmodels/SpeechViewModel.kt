package com.swanky.teachit.viewmodels

import Event
import android.app.Application
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swanky.teachit.db.AppDatabase
import com.swanky.teachit.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.swanky.teachit.db.entity.SpeechRecord
import com.swanky.teachit.repositories.SpeechRepository

class SpeechViewModel(application: Application) : AndroidViewModel(application) {

    private val speechRepository: SpeechRepository

    // Veritabanından gelen tüm kayıtlar
    val allRecords: LiveData<List<SpeechRecord>>

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage
    private val _noResultEvent = MutableLiveData<Event<Unit>>()
    val noResultEvent: LiveData<Event<Unit>> = _noResultEvent

    private val _navigateToChat = MutableLiveData<Event<String>>()
    val navigateToChat: LiveData<Event<String>> = _navigateToChat

    init {
        val dao = AppDatabase.getDatabase(application).getDao()
        speechRepository = SpeechRepository(dao) // Yeni Repository'yi oluştur
        allRecords = speechRepository.allSpeechRecordsLiveData
    }

    fun processSpeechResult(resultCode: Int, data: Intent?) {
        _isLoading.value = false
        if (resultCode == android.app.Activity.RESULT_OK && data != null) {
            val speechResult: ArrayList<String>? =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!speechResult.isNullOrEmpty()) {
                val recognizedText = speechResult[0]
                insertRecord(recognizedText)
                _navigateToChat.value = Event(recognizedText)
            } else {
                _noResultEvent.value = Event(Unit)
            }
        } else { /* Hata veya iptal */ }
    }

    // Veritabanına yeni kayıt ekler ve sonucu işler
    private fun insertRecord(text: String) = viewModelScope.launch {
        _isLoading.value = true
        val result = speechRepository.insertSpeechRecord(SpeechRecord(text = text))
        _isLoading.value = false
        result.onFailure { exception ->
            _errorMessage.value = Event("Kayıt hatası: ${exception.localizedMessage ?: "Bilinmeyen hata"}")
        }
    }

    // Konuşma tanıma desteği yoksa hata bildirir
    fun notifySpeechNotSupported(message: String) {
        _isLoading.value = false
        _errorMessage.value = Event(message)
    }

    // Konuşma tanıma başladığında yüklenme durumunu ayarlar
    fun startProcessing() {
        _isLoading.value = true
    }

    // Kayıt silme işlemleri (Result kontrolü ile)
    fun deleteRecord(recordId: Long) = viewModelScope.launch {
        _isLoading.value = true
        val result = speechRepository.deleteSpeechRecordById(recordId)
        _isLoading.value = false
        result.onFailure { exception ->
            _errorMessage.value = Event("Silme hatası: ${exception.localizedMessage ?: "Bilinmeyen hata"}")
        }
    }

    fun deleteAllRecords() = viewModelScope.launch {
        _isLoading.value = true
        val result = speechRepository.deleteAllSpeechRecords()
        _isLoading.value = false
        result.onFailure { exception ->
            _errorMessage.value = Event("Tümünü silme hatası: ${exception.localizedMessage ?: "Bilinmeyen hata"}")
        }
    }
}
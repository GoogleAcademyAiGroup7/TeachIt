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

    // Repository manuel olarak oluşturulacak
    private val speechRepository: SpeechRepository // Yeni Repository tipini kullan

    // Veritabanından gelen tüm kayıtlar
    val allRecords: LiveData<List<SpeechRecord>>

    // --- UI Durumları için LiveData'lar ---
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage
    private val _noResultEvent = MutableLiveData<Event<Unit>>()
    val noResultEvent: LiveData<Event<Unit>> = _noResultEvent

    // --- Navigasyon ---
    private val _navigateToChat = MutableLiveData<Event<String>>()
    val navigateToChat: LiveData<Event<String>> = _navigateToChat

    // Tanınan metinlerin geçici listesi (Fragment'taki observer bunu kullanıyor)
    // Bu liste artık doğrudan veritabanından gelen `allRecords` ile yönetiliyor.
    // Eğer hala ayrı bir liste tutmak istiyorsanız bu kalabilir, ama genellikle
    // `allRecords`'ı observe etmek yeterlidir. Şimdilik yoruma alıyorum.
    // private val _recognizedTexts = MutableLiveData<List<String>>(emptyList())
    // val recognizedTexts: LiveData<List<String>> = _recognizedTexts

    init {
        // Repository'yi manuel olarak oluştur
        val dao = AppDatabase.getDatabase(application).getDao()
        speechRepository = SpeechRepository(dao) // Yeni Repository'yi oluştur
        // LiveData repository'den alınıyor
        allRecords = speechRepository.allSpeechRecordsLiveData
    }

    /**
     * Konuşma tanıma sonucunu işler. Başarılıysa veritabanına kaydeder ve navigasyonu tetikler.
     */
    fun processSpeechResult(resultCode: Int, data: Intent?) {
        _isLoading.value = false
        if (resultCode == android.app.Activity.RESULT_OK && data != null) {
            val speechResult: ArrayList<String>? =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!speechResult.isNullOrEmpty()) {
                val recognizedText = speechResult[0]
                // 1. Veritabanına kaydet (arka planda)
                insertRecord(recognizedText)
                // 2. Navigasyonu tetikle
                _navigateToChat.value = Event(recognizedText)
            } else {
                _noResultEvent.value = Event(Unit)
            }
        } else { /* Hata veya iptal */ }
    }

    // Veritabanına yeni kayıt ekler ve sonucu işler
    private fun insertRecord(text: String) = viewModelScope.launch {
        _isLoading.value = true
        val result = speechRepository.insertSpeechRecord(SpeechRecord(text = text)) // Yeni Repository metodunu kullan
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
        val result = speechRepository.deleteSpeechRecordById(recordId) // Yeni Repository metodunu kullan
        _isLoading.value = false
        result.onFailure { exception ->
            _errorMessage.value = Event("Silme hatası: ${exception.localizedMessage ?: "Bilinmeyen hata"}")
        }
    }

    fun deleteAllRecords() = viewModelScope.launch {
        _isLoading.value = true
        val result = speechRepository.deleteAllSpeechRecords() // Yeni Repository metodunu kullan
        _isLoading.value = false
        result.onFailure { exception ->
            _errorMessage.value = Event("Tümünü silme hatası: ${exception.localizedMessage ?: "Bilinmeyen hata"}")
        }
    }
}
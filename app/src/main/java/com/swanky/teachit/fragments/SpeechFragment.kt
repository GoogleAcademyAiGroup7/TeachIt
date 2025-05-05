package com.swanky.teachit.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.swanky.teachit.R
import com.swanky.teachit.adapters.SpeechListAdapter
import com.swanky.teachit.databinding.FragmentSpeechBinding
import com.swanky.teachit.db.entity.SpeechRecord
import com.swanky.teachit.viewmodels.SpeechViewModel

class SpeechFragment : Fragment() {

    private var _binding: FragmentSpeechBinding? = null
    private val binding get() = _binding!!

    // ViewModel'i al (Hilt olmadan da çalışır)
    private val viewModel: SpeechViewModel by viewModels()

    private lateinit var speechResultLauncher: ActivityResultLauncher<Intent>
    // RecyclerView için Adapter (SpeechRecord ve anonim DiffUtil ile çalışan)
    private lateinit var speechListAdapter: SpeechListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speechResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.processSpeechResult(result.resultCode, result.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeechBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView() // RecyclerView ve adapter'ı ayarla
        binding.btnStartSpeech.setOnClickListener {
            startSpeechRecognition() // Konuşma tanımayı başlat
        }
        observeViewModel() // ViewModel'i dinlemeye başla
    }

    /**
     * RecyclerView'ı ve adapter'ını ayarlar. Adapter oluşturulurken tıklama dinleyicisi tanımlanır.
     */
    private fun setupRecyclerView() {
        // Adapter'ı oluştururken tıklama olayını (lambda) tanımla
        // Bu adapter (ID: adapter_speech_list_clickable) anonim DiffUtil kullanır.
        speechListAdapter = SpeechListAdapter { clickedRecord ->
            // Bir öğeye tıklandığında bu kod çalışır
            Log.i("SpeechFragment", "Card clicked: ${clickedRecord.text}")
            navigateToChatWithRecord(clickedRecord) // Navigasyon fonksiyonunu çağır
        }
        binding.rvSpeechList.apply {
            adapter = speechListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /**
     * ViewModel'deki LiveData değişikliklerini gözlemler ve UI'ı buna göre günceller.
     */
    private fun observeViewModel() {
        // 1. Veritabanından gelen kayıt listesini dinle (LiveData<List<SpeechRecord>>)
        viewModel.allRecords.observe(viewLifecycleOwner) { records ->
            Log.d("SpeechFragment", "Observed ${records?.size ?: 0} records from database.")
            // Adapter'a güncel List<SpeechRecord> listesini gönder
            speechListAdapter.submitList(records)
        }

        // 2. Otomatik Navigasyon olayını dinle (Konuşma sonrası)
        viewModel.navigateToChat.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { recognizedText ->
                Log.i("SpeechFragment", "Automatic navigation event received. Navigating to ChatFragment with text: $recognizedText")
                // Otomatik navigasyon için metni kullanarak yönlendir
                navigateToChatWithText(recognizedText)
            }
        }

        // 3. Yüklenme durumunu dinle
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("SpeechFragment", "isLoading state: $isLoading")
            binding.progressBar.isVisible = isLoading
            binding.btnStartSpeech.isEnabled = !isLoading
        }

        // 4. Hata mesajlarını dinle
        viewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Log.w("SpeechFragment", "Error message received: $message")
                showToast(message)
            }
        }

        // 5. Sonuç yok olayını dinle
        viewModel.noResultEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Log.d("SpeechFragment", "No speech result event received.")
                showToast(getString(R.string.no_speech_result))
            }
        }
    }

    /**
     * Verilen SpeechRecord'un metniyle ChatFragment'a navigasyon yapar.
     * @param record Navigasyon için kullanılacak SpeechRecord.
     */
    private fun navigateToChatWithRecord(record: SpeechRecord) {
        navigateToChatWithText(record.text) // Metni alıp diğer fonksiyona gönder
    }

    /**
     * Verilen metinle ChatFragment'a navigasyon yapar.
     * @param text ChatFragment'a gönderilecek metin.
     */
    private fun navigateToChatWithText(text: String) {
        try {
            // Navigasyon action'ını oluştur ve tetikle (ID'yi kontrol et)
            val action = SpeechFragmentDirections.speechToChat(text)
            findNavController().navigate(action)
        } catch (e: Exception) {
            // Navigasyon sırasında oluşabilecek hataları yakala
            val errorMsg = "Navigasyon başlatılamadı: ${e.message}"
            Log.e("SpeechFragment", errorMsg, e)
            showToast(errorMsg)
        }
    }


    /**
     * Android'in konuşma tanıma Intent'ini başlatır.
     */
    private fun startSpeechRecognition() {
        Log.d("SpeechFragment", "Start speech recognition button clicked.")
        viewModel.startProcessing() // ViewModel'e işlemin başladığını bildir

        // Konuşma tanıma Intent'ini hazırla
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR") // Dili Türkçe olarak ayarla
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt)) // Kullanıcıya gösterilecek ipucu
        }

        // Intent'i başlatmayı dene
        try {
            speechResultLauncher.launch(speechIntent)
        } catch (e: ActivityNotFoundException) {
            // Cihaz konuşma tanımayı desteklemiyorsa veya aktivite bulunamazsa
            val errorMsg = getString(R.string.speech_not_supported)
            Log.e("SpeechFragment", "Speech recognition not supported on this device.", e)
            viewModel.notifySpeechNotSupported(errorMsg) // Hatayı ViewModel'e bildir
        }
    }

    /**
     * Ekranda kısa süreli bir bilgilendirme mesajı (Toast) gösterir.
     * @param message Gösterilecek mesaj.
     */
    private fun showToast(message: String) {
        // Fragment'ın context'i hala geçerliyse Toast'ı göster
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("SpeechFragment", "onDestroyView called, cleaning up view binding.")
        // ViewBinding referansını temizle (Bellek sızıntılarını önlemek için ÇOK ÖNEMLİ)
        _binding = null
    }
}
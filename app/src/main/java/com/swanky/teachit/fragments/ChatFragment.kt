package com.swanky.teachit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs // Safe Args için import
import com.swanky.teachit.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    // Safe Args kullanarak argümanları al
    private val args: ChatFragmentArgs by navArgs()

    // TODO: ChatViewModel oluşturulacak

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gelen argümanı (ilk prompt) al ve TextView'a yazdır
        val initialPrompt = args.initialPrompt
        binding.tvInitialPrompt.text = initialPrompt

        // TODO: Bu prompt'u alıp ChatViewModel aracılığıyla Gemini'ye gönder
        // startGeminiConversation(initialPrompt)
        binding.tvGeminiResponse.text = "Gemini ile konuşma başlatılıyor: \"$initialPrompt\"" // Geçici
    }

    // TODO: Gemini API ile konuşma mantığını ekle
    // private fun startGeminiConversation(prompt: String) { ... }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

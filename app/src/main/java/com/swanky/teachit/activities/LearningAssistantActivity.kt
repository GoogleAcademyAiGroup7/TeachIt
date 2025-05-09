package com.swanky.teachit.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.swanky.teachit.databinding.ActivityLearningAssistantBinding
import com.swanky.teachit.databinding.BottomSheetAddTopicBinding
import com.swanky.teachit.databinding.BottomSheetAiResponseBinding
import com.swanky.teachit.models.Evaluation
import com.swanky.teachit.models.Topic
import com.swanky.teachit.utils.ApiKeyUtil
import com.swanky.teachit.utils.hideWithAnimated
import com.swanky.teachit.utils.showWithAnimated
import com.swanky.teachit.viewmodels.LearningAsistantViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LearningAssistantActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityLearningAssistantBinding
    private lateinit var speechLauncher: ActivityResultLauncher<Intent>
    private var isFirstSpeech = true
    private var selectedTopic: Topic? = null
    private val viewModel: LearningAsistantViewmodel by viewModels()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomDialogBinding: BottomSheetAiResponseBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityLearningAssistantBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        dataBinding.master = this

        observeLiveData()
        registerLauncher()

       selectedTopic = intent.getSerializableExtra("selectedTopic") as? Topic
        selectedTopic?.let {
            putDataToLayout(it)
        }

        dataBinding.speechButton.setOnClickListener {
            startSpeechRecognition()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun putDataToLayout(selectedTopic: Topic) {
        dataBinding.asistantTopicTitle.text = selectedTopic.title
        dataBinding.asistantWelcomeTxt.text =
            "${selectedTopic.title} konusunu anlatmaya hazır mısın? Seni dikkatle dinleyeceğim ve sonrasında neler harika, neleri geliştirebilirsin birlikte keşfedeceğiz. Hazırsan alttaki düğmeye dokun ve başlayalım!"
    }

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Konuşmaya başlayabilirsin...")
        }
        try {
            speechLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Cihazınız konuşma tanımayı desteklemiyor", Toast.LENGTH_LONG)
                .show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun registerLauncher() {
        speechLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val spokenText = result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.get(0)
                val currentText = dataBinding.asistanInputEditText.text.toString()
                dataBinding.asistanInputEditText.setText("$currentText $spokenText")
                checkIfFirstSpeech()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkIfFirstSpeech() {
        if (isFirstSpeech) {
            dataBinding.asistantWelcomeTxt.text = "Eklemek istediğin şeyler varsa tekrar konuşma butonuna basabilir veya el ile düzenleyebilirsin ^^"
            isFirstSpeech = false
            dataBinding.asistantSendAiButton.showWithAnimated(rootView = dataBinding.asistantCardView)
            dataBinding.asistantTextInput.showWithAnimated(rootView = dataBinding.asistantCardView)
            defineSendButton()
        }
    }

    private fun defineSendButton() {
        dataBinding.asistantSendAiButton.setOnClickListener {
            val userAnswer = dataBinding.asistanInputEditText.text.toString()

            if (userAnswer.isNotEmpty()) {
                // Send Ai
                viewModel.sendAi(selectedTopic!!, userAnswer)
                defineBottomSheetDialog()

            } else {
                Toast.makeText(this, "Göndermek için konuyu anlatmalısın...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.evaluation.observe(this) { evaluation ->
            if (evaluation != null) {
                updateUiBottomSheet(evaluation)
            } else {
                //Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun defineBottomSheetDialog() {
        bottomDialogBinding = BottomSheetAiResponseBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog = BottomSheetDialog(this)

        bottomSheetDialog.apply {
            setContentView(bottomDialogBinding.root)
            this.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            setCancelable(false)
        }
        bottomSheetDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUiBottomSheet(evaluation: Evaluation) {
        bottomSheetDialog.setCancelable(true)

        bottomDialogBinding.aiLoadingLayout.hideWithAnimated(bottomDialogBinding.bottomSheetRoot)
        bottomDialogBinding.resultLayout.showWithAnimated(bottomDialogBinding.bottomSheetRoot)

        bottomDialogBinding.missingInfoText.text = evaluation.missingInfo
        bottomDialogBinding.explanationQualityText.text = evaluation.explanationQuality
        val suggestionsList = evaluation.suggestions
        val formattedSuggestions = suggestionsList.joinToString("\n") { "• $it" }
        bottomDialogBinding.suggestionsTxt.text = formattedSuggestions
        bottomDialogBinding.aiResultScoreTxt.text = evaluation.score.toString()
    }



    fun previousPage() {
        onBackPressedDispatcher.onBackPressed()
    }

}
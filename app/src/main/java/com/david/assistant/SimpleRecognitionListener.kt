package com.david.assistant

import android.os.Bundle
import android.speech.RecognitionListener

class SimpleRecognitionListener(private val onResult: (String) -> Unit) : RecognitionListener {
    override fun onResults(results: Bundle) { results.getStringArrayList("results_recognition")?.firstOrNull()?.let(onResult) }
    override fun onReadyForSpeech(p: Bundle?) {} ; override fun onBeginningOfSpeech() {} ; override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {} ; override fun onEndOfSpeech() {} ; override fun onError(error: Int) {}
    override fun onPartialResults(partialResults: Bundle?) {} ; override fun onEvent(eventType: Int, params: Bundle?) {}
}

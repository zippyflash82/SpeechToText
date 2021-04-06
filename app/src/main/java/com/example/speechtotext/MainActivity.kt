package com.example.speechtotext

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var speechRecognizer: SpeechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
                et_output.setText("")
                et_output.hint = "Listening...!"
            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {
                btn_speech.setImageResource(R.drawable.ic_mic_blue)
            }

            override fun onEndOfSpeech() {

            }

            override fun onError(error: Int) {
                btn_speech.setImageResource(R.drawable.ic_mic_blue)
                et_output.hint = "Tap to Speak"
                Toast.makeText(applicationContext, errerHandler(error), Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                btn_speech.setImageResource(R.drawable.ic_mic_blue)
                val data: ArrayList<String>? =
                    results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                et_output.setText(data!![0])
            }

            override fun onPartialResults(partialResults: Bundle?) {
                btn_speech.setImageResource(R.drawable.ic_mic_blue)
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }
        })


        btn_speech.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action === MotionEvent.ACTION_UP) {
                btn_speech.setImageResource(R.drawable.ic_mic_blue)
                speechRecognizer.stopListening()
            }
            if (motionEvent.action === MotionEvent.ACTION_DOWN) {
                btn_speech.setImageResource(R.drawable.ic_mic_red)
                speechRecognizer.startListening(speechRecognizerIntent)
            }
            false
        }
    }

    fun checkPermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    if (report.areAllPermissionsGranted()) {
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }
    fun errerHandler(errorCode: Int): String {
        when (errorCode) {
            1 -> {
                return "NETWORK TIMEOUT"
            }
            2 -> {
                return "NETWORK ERROR"
            }
            3 -> {
                return "AUDIO ERROR"
            }
            4 -> {
                return "SERVER ERROR"
            }
            5 -> {
                return "CLIENT ERROR"
            }
            6 -> {
                return "SPEECH TIMEOUT"
            }
            7 -> {
                return "NO MATCH"
            }
            8 -> {
                return "RECOGNIZER BUSY"
            }
            9 -> {
                return "INSUFFICIENT_PERMISSIONS"
            }
            10 -> {
                return "TOO MANY REQUESTS"
            }

            11 -> {
                return "SERVER DISCONNECTED"
            }
            else -> {
            }
        }
        return "UNKNOWN ERROR"
    }


}
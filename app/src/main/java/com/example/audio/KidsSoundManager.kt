package com.example.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import java.util.Locale

class KidsSoundManager(context: Context) : TextToSpeech.OnInitListener {

    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null
    private var ttsReady = false

    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 70)
    } catch (_: Exception) {
        null
    }

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    init {
        try {
            tts = TextToSpeech(appContext, this)
        } catch (_: Exception) {
            ttsReady = false
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.let { engine ->
                val result = engine.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.setPitch(1.05f) // Cheerful clear tone
                    engine.setSpeechRate(0.88f) // Slightly slower for kids phonics
                    ttsReady = true
                }
            }
        }
    }

    fun speakWord(word: String, enabled: Boolean) {
        if (!enabled || !ttsReady) return
        try {
            tts?.speak(word.lowercase(), TextToSpeech.QUEUE_FLUSH, null, "WORD_PRONUNCIATION_$word")
        } catch (_: Exception) {
            // Ignore error
        }
    }

    fun playWordFoundTone(soundEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
            } catch (_: Exception) {
                // Ignore
            }
        }
        playGentleHaptic()
    }

    fun playVictoryFanfare(soundEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 350)
            } catch (_: Exception) {
                // Ignore
            }
        }
        playCelebrationHaptic()
    }

    private fun playGentleHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(40)
            }
        } catch (_: Exception) {
            // Fallback
        }
    }

    private fun playCelebrationHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 70, 70, 120)
                val amplitudes = intArrayOf(0, 180, 0, 255)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 70, 70, 120), -1)
            }
        } catch (_: Exception) {
            // Fallback
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (_: Exception) {
            // Ignore
        }
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {
            // Ignore
        }
    }
}

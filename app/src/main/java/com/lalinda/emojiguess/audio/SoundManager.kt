package com.lalinda.emojiguess.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.SoundPool
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

enum class BgmContext { Home, StageSelect, Gameplay, Hardcore }

class SoundManager(private val context: Context) {

    private var soundPool: SoundPool? = null
    private var correctSoundId: Int = 0
    private var wrongSoundId: Int = 0
    private var clickSoundId: Int = 0
    private var gameOverSoundId: Int = 0
    private var levelUpSoundId: Int = 0

    var isSoundEnabled: Boolean = true
    var isBgmEnabled: Boolean = true
        set(value) {
            field = value
            if (value) startBgm() else stopBgm()
        }

    private var currentBgmContext: BgmContext = BgmContext.Home

    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var bgmJob: Job? = null
    private val sampleRate = 22050

    init {
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build()

            correctSoundId = loadRawSoundIfExists("correct")
            wrongSoundId = loadRawSoundIfExists("wrong")
            clickSoundId = loadRawSoundIfExists("click")
            gameOverSoundId = loadRawSoundIfExists("game_over")
            levelUpSoundId = loadRawSoundIfExists("level_up")
        } catch (e: Exception) {
            Log.e("SoundManager", "Error initializing SoundPool", e)
        }
    }

    private fun loadRawSoundIfExists(resName: String): Int {
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        return if (resId != 0 && soundPool != null) {
            try {
                soundPool!!.load(context, resId, 1)
            } catch (e: Exception) {
                0
            }
        } else {
            0
        }
    }

    fun setBgmContext(contextMode: BgmContext) {
        if (currentBgmContext == contextMode && bgmJob?.isActive == true) return
        currentBgmContext = contextMode
        if (isBgmEnabled) {
            stopBgm()
            startBgm()
        }
    }

    fun playGameStartSound() {
        if (!isSoundEnabled) return
        // Energetic ascending game start fanfare: E5 -> G5 -> C6 -> E6
        playSynthesizedArpeggio(listOf(659.25, 783.99, 1046.50, 1318.51), 75)
    }

    fun playStageUnlockedSound() {
        if (!isSoundEnabled) return
        // Celebratory stage unlocked fanfare: C5 -> E5 -> G5 -> B5 -> C6
        playSynthesizedArpeggio(listOf(523.25, 659.25, 783.99, 987.77, 1046.50), 90)
    }

    fun playHintSound() {
        if (!isSoundEnabled) return
        // Magical sparkle hint chime: C6 -> E6 -> G6
        playSynthesizedArpeggio(listOf(1046.50, 1318.51, 1567.98), 60)
    }

    fun playCorrectSound() {
        if (!isSoundEnabled) return
        if (correctSoundId != 0) {
            playSound(correctSoundId)
        } else {
            // Synthesize C-major arpeggio: C5 -> E5 -> G5 -> C6
            playSynthesizedArpeggio(listOf(523.25, 659.25, 783.99, 1046.50), 90)
        }
    }

    fun playWrongSound() {
        if (!isSoundEnabled) return
        if (wrongSoundId != 0) {
            playSound(wrongSoundId)
        } else {
            // Synthesize descending low tone: 320Hz -> 180Hz
            playSynthesizedSweep(320.0, 180.0, 250)
        }
    }

    fun playClickSound() {
        if (!isSoundEnabled) return
        if (clickSoundId != 0) {
            playSound(clickSoundId)
        } else {
            // Short pop tone: 880Hz, 35ms
            playSynthesizedTone(880.0, 35)
        }
    }

    fun playGameOverSound() {
        if (!isSoundEnabled) return
        if (gameOverSoundId != 0) {
            playSound(gameOverSoundId)
        } else {
            // Sad minor arpeggio: A4 -> F4 -> D4 -> A3
            playSynthesizedArpeggio(listOf(440.0, 349.23, 293.66, 220.0), 160)
        }
    }

    fun playSadSound() {
        if (!isSoundEnabled) return
        // Sad low pitch sweep: 220Hz -> 110Hz -> 80Hz
        playSynthesizedSweep(220.0, 80.0, 400)
    }

    fun playRefillSound() {
        if (!isSoundEnabled) return
        // Bright pleasant rising chime: C5 -> E5 -> G5 -> C6 -> E6
        playSynthesizedArpeggio(listOf(523.25, 659.25, 783.99, 1046.50, 1318.51), 70)
    }

    fun playVictorySound() {
        if (!isSoundEnabled) return
        // Grand victory fanfare: G4 -> C5 -> E5 -> G5 -> C6
        playSynthesizedArpeggio(listOf(392.0, 523.25, 659.25, 783.99, 1046.50), 100)
    }

    fun playLevelUpSound() {
        if (!isSoundEnabled) return
        if (levelUpSoundId != 0) {
            playSound(levelUpSoundId)
        } else {
            // Victory Fanfare: G4 -> C5 -> E5 -> G5
            playSynthesizedArpeggio(listOf(392.0, 523.25, 659.25, 783.99), 110)
        }
    }

    fun startBgm() {
        if (bgmJob?.isActive == true) return
        bgmJob = scope.launch {
            val (melody, tempo) = when (currentBgmContext) {
                BgmContext.Home -> Pair(
                    listOf(
                        523.25, 659.25, 783.99, 659.25,
                        587.33, 698.46, 880.00, 698.46,
                        659.25, 783.99, 1046.50, 783.99,
                        587.33, 523.25, 440.00, 493.88
                    ),
                    210L
                )
                BgmContext.StageSelect -> Pair(
                    listOf(
                        440.00, 523.25, 659.25, 523.25,
                        392.00, 493.88, 587.33, 493.88,
                        349.23, 440.00, 523.25, 440.00,
                        329.63, 392.00, 493.88, 392.00
                    ),
                    220L
                )
                BgmContext.Gameplay -> Pair(
                    listOf(
                        659.25, 783.99, 1046.50, 880.00,
                        783.99, 659.25, 587.33, 659.25,
                        783.99, 880.00, 1046.50, 1174.66,
                        1046.50, 880.00, 783.99, 659.25
                    ),
                    180L
                )
                BgmContext.Hardcore -> Pair(
                    listOf(
                        440.00, 523.25, 659.25, 587.33,
                        659.25, 783.99, 880.00, 783.99,
                        880.00, 1046.50, 1174.66, 1046.50,
                        880.00, 783.99, 659.25, 523.25
                    ),
                    120L
                )
            }

            var index = 0
            while (isActive && isBgmEnabled) {
                val freq = melody[index % melody.size]
                playSoftTone(freq, (tempo * 0.9).toInt(), 0.07f)
                delay(tempo)
                index++
            }
        }
    }

    fun stopBgm() {
        bgmJob?.cancel()
        bgmJob = null
    }

    fun pauseBgm() {
        stopBgm()
    }

    fun resumeBgm() {
        if (isBgmEnabled) {
            stopBgm()
            startBgm()
        }
    }

    private fun playSound(soundId: Int) {
        try {
            soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing sound ID $soundId", e)
        }
    }

    private fun playSynthesizedTone(frequency: Double, durationMs: Int, volume: Float = 0.3f) {
        scope.launch {
            try {
                val numSamples = (durationMs * sampleRate) / 1000
                val sample = ByteArray(numSamples * 2)

                for (i in 0 until numSamples) {
                    val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
                    val value = (sin(angle) * 32767 * volume).toInt().coerceIn(-32768, 32767).toShort()
                    sample[i * 2] = (value.toInt() and 0x00ff).toByte()
                    sample[i * 2 + 1] = (value.toInt() and 0xff00 shr 8).toByte()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(sample.size)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(sample, 0, sample.size)
                audioTrack.play()
                delay(durationMs.toLong() + 50)
                audioTrack.release()
            } catch (e: Exception) {
                Log.e("SoundManager", "Error in synth tone", e)
            }
        }
    }

    private fun playSoftTone(frequency: Double, durationMs: Int, volume: Float) {
        try {
            val numSamples = (durationMs * sampleRate) / 1000
            val sample = ByteArray(numSamples * 2)

            for (i in 0 until numSamples) {
                val envelope = when {
                    i < numSamples * 0.1 -> i / (numSamples * 0.1f)
                    i > numSamples * 0.8 -> (numSamples - i) / (numSamples * 0.2f)
                    else -> 1.0f
                }
                val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
                val value = (sin(angle) * envelope * 32767 * volume).toInt().coerceIn(-32768, 32767).toShort()
                sample[i * 2] = (value.toInt() and 0x00ff).toByte()
                sample[i * 2 + 1] = (value.toInt() and 0xff00 shr 8).toByte()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(sample.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(sample, 0, sample.size)
            audioTrack.play()
            scope.launch {
                delay(durationMs.toLong() + 20)
                audioTrack.release()
            }
        } catch (e: Exception) {
            // Ignore background audio glitch
        }
    }

    private fun playSynthesizedArpeggio(frequencies: List<Double>, noteDurationMs: Int) {
        scope.launch {
            for (freq in frequencies) {
                playSynthesizedTone(freq, noteDurationMs, 0.35f)
                delay(noteDurationMs.toLong())
            }
        }
    }

    private fun playSynthesizedSweep(startFreq: Double, endFreq: Double, durationMs: Int) {
        scope.launch {
            try {
                val numSamples = (durationMs * sampleRate) / 1000
                val sample = ByteArray(numSamples * 2)

                for (i in 0 until numSamples) {
                    val progress = i.toDouble() / numSamples
                    val currentFreq = startFreq + (endFreq - startFreq) * progress
                    val angle = 2.0 * Math.PI * i / (sampleRate / currentFreq)
                    val value = (sin(angle) * 32767 * 0.3f).toInt().coerceIn(-32768, 32767).toShort()
                    sample[i * 2] = (value.toInt() and 0x00ff).toByte()
                    sample[i * 2 + 1] = (value.toInt() and 0xff00 shr 8).toByte()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(sample.size)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(sample, 0, sample.size)
                audioTrack.play()
                delay(durationMs.toLong() + 50)
                audioTrack.release()
            } catch (e: Exception) {
                Log.e("SoundManager", "Error in synth sweep", e)
            }
        }
    }

    fun release() {
        stopBgm()
        soundPool?.release()
        soundPool = null
    }
}

package com.amartinez.musicplayer.presentation.player.controller

import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.util.Log
import com.amartinez.musicplayer.domain.model.Result
import java.util.*

class MediaController private constructor() {
    private var audioTrackPlayer: AudioTrack? = null
    private val playerSongDetailSync = Any()
    private val progressTimerSync = Any()
    private var progressTimer: Timer? = null
    private var audioPlayer: MediaPlayer? = null
    private var current: Result? = null
    private var eventHandler: EventHandler? = null
    private var isPaused = true
    fun playAudio(result: Result) {
        if ((audioTrackPlayer != null || audioPlayer != null) && current != null && current!!.trackId.equals(
                result.trackId
            )
        ) {
            if (isPaused) {
                resumeAudio()
                isPaused = false
                return
            }
        }
        cleanupPlayer()
        try {
            current = result
            audioPlayer = MediaPlayer()
            audioPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            audioPlayer!!.setDataSource(result.previewUrl)
            audioPlayer!!.prepareAsync()
            audioPlayer!!.setOnPreparedListener { mp: MediaPlayer ->
                mp.start()
                eventHandler?.playPauseEvent(true)
            }
        } catch (e: Exception) {}
    }

    private fun stopProgressTimer() {
        synchronized(progressTimerSync) {
            if (progressTimer != null) {
                try {
                    progressTimer!!.cancel()
                    progressTimer = null
                } catch (e: Exception) {
                    Log.e("tmessages", e.toString())
                }
            }
        }
    }

    fun pauseAudio(): Boolean {
        if (audioTrackPlayer == null && audioPlayer == null) {
            return false
        }
        stopProgressTimer()
        try {
            if (audioPlayer != null) {
                audioPlayer!!.pause()
                eventHandler?.playPauseEvent(false)
            } else if (audioTrackPlayer != null) {
                audioTrackPlayer!!.pause()
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun resumeAudio(): Boolean {
        if (audioTrackPlayer == null && audioPlayer == null) {
            return false
        }
        try {
            if (audioPlayer != null) {
                audioPlayer!!.start()
                eventHandler?.playPauseEvent(true)
            } else {
                audioTrackPlayer!!.play()
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun cleanupPlayer() {
        pauseAudio()
        if (audioPlayer != null) {
            try {
                audioPlayer!!.reset()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                audioPlayer!!.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                audioPlayer!!.release()
                audioPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (audioTrackPlayer != null) {
            synchronized(playerSongDetailSync) {
                try {
                    audioTrackPlayer!!.pause()
                    audioTrackPlayer!!.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    audioTrackPlayer!!.release()
                    audioTrackPlayer = null
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        stopProgressTimer()
    }

    fun setEventHandler(eventHandler: EventHandler?) {
        this.eventHandler = eventHandler
    }

    companion object {
        @Volatile
        var instance: MediaController? = null
            get() {
                var localInstance = field
                if (localInstance == null) {
                    synchronized(MediaController::class.java) {
                        localInstance = field
                        if (localInstance == null) {
                            localInstance = MediaController()
                            field = localInstance
                        }
                    }
                }
                return localInstance
            }
            private set
    }
}
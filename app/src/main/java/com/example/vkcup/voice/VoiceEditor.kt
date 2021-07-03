package com.example.vkcup.voice

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.example.vkcup.R


class VoiceEditor : AppCompatActivity() {
    private val slowSpeed = 0.6f
    private val fastSpeed = 1.5f

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var fileName: String
    private lateinit var play: ImageView
    private lateinit var checkbox1: CheckBox
    private lateinit var checkbox2: CheckBox
    private var mode: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_editor)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        title = "Voice editor"
        fileName = Environment.getExternalStorageDirectory().toString() + "/record.mpeg4"
        play = findViewById(R.id.play)
        seekBar = findViewById(R.id.seekbar)
        checkbox1 = findViewById(R.id.checkbox1)
        checkbox2 = findViewById(R.id.checkbox2)
        checkbox1.setOnClickListener {
            pause()
        }
        checkbox2.setOnClickListener {
            pause()
        }
        play.setOnClickListener {
            when (mode) {
                1 -> play()
                2 -> pause()
                3 -> resume()
            }
        }
    }

    private fun play() {
        mode = 2
        play.setImageResource(R.drawable.ic_baseline_pause_24)
        try {
            releasePlayer()
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(fileName)
            mediaPlayer!!.prepare()
            mediaPlayer!!.setOnCompletionListener {
                play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                mode = 1
            }
            seekBar.max = mediaPlayer!!.duration / 1000
            val mHandler = Handler()
            this@VoiceEditor.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition: Int = mediaPlayer!!.currentPosition / 1000
                        seekBar.progress = mCurrentPosition
                    }
                    mHandler.postDelayed(this, 1000)
                }
            })

            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (mediaPlayer != null && fromUser) {
                        play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                        mode = 3
                        mediaPlayer!!.pause()
                        mediaPlayer!!.seekTo(progress * 1000)
                    }
                }
            })
            resume()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pause() {
        if (mediaPlayer == null) return
        mode = 3
        play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    private fun resume() {
        mode = 2
        play.setImageResource(R.drawable.ic_baseline_pause_24)
        checkClick()
        if (mediaPlayer != null) {
            mediaPlayer!!.start()
        }
    }

    private fun checkClick() {
        if (mediaPlayer ==  null) return
        if (checkbox1.isChecked && checkbox2.isChecked) {
            mediaPlayer!!.playbackParams = mediaPlayer!!.playbackParams.setSpeed(1f)
        } else {
            if (checkbox1.isChecked) {
                mediaPlayer!!.playbackParams = mediaPlayer!!.playbackParams.setSpeed(fastSpeed)
            } else {
                if (checkbox2.isChecked) {
                    mediaPlayer!!.playbackParams = mediaPlayer!!.playbackParams.setSpeed(slowSpeed)
                } else {
                    mediaPlayer!!.playbackParams = mediaPlayer!!.playbackParams.setSpeed(1f)
                }
            }
        }
    }

    private fun releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pause()
        releasePlayer()
    }

}
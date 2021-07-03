package com.example.vkcup.voice

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.SoundPool
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vkcup.R
import com.example.vkcup.crosszero.CrossZeroGame
import java.io.File
import java.util.concurrent.Executors


class Voice : Activity() {
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var fileName: String
    private val myRequestId = 10
    private lateinit var btm: ImageButton
    private lateinit var textView: TextView
    private lateinit var plus: ImageView
    private lateinit var minus: ImageView
    private var mode: Int = 1

    private val begin = "Запишите голосовое"
    private val find = "Идет запись"
    private val find1 = "Идет запись."
    private val find2 = "Идет запись.."
    private val find3 = "Идет запись..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fileName = Environment.getExternalStorageDirectory().toString() + "/record.mpeg4"
        btm = findViewById(R.id.button)
        textView = findViewById(R.id.text)
        plus = findViewById(R.id.plus)
        minus = findViewById(R.id.minus)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                myRequestId
            )
        } else {
            textView.text = begin
            btm.setOnClickListener {
                when (mode) {
                    1 -> record()
                    2 -> stop()
                    3 -> playRec()
                    4 -> stopRec()
                    5 -> resumeRec()
                }
            }
            minus.setOnClickListener {
                clear()
            }
            plus.setOnClickListener {
                stopRec()
                val intent = Intent(this@Voice, VoiceEditor::class.java)
                startActivity(intent)
            }
        }
    }

    private fun clear() {
        minus.visibility = View.INVISIBLE
        plus.visibility = View.INVISIBLE
        textView.text = begin
        btm.setImageResource(R.drawable.ic_baseline_mic_24)
        mode = 1
    }

    private fun record() {
        btm.setImageResource(R.drawable.ic_baseline_pause_24)
        animation()
        mode = 2
        try {
            releaseRecorder()
            val outFile = File(fileName)
            if (outFile.exists()) {
                outFile.delete()
            }
            mediaRecorder = MediaRecorder()
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.setOutputFile(fileName)
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun animation() {
        textView.text = find
        val pool = Executors.newSingleThreadExecutor()
        pool.submit {
            while (true) {
                if (textView.text == find) {
                    textView.text = find1
                    Thread.sleep(500)
                }
                if (textView.text == find1) {
                    textView.text = find2
                    Thread.sleep(500)
                }
                if (textView.text == find2) {
                    textView.text = find3
                    Thread.sleep(500)
                }
                if (textView.text == find3) {
                    textView.text = find
                    Thread.sleep(500)
                }
                if (mode == 3) {
                    break
                }
            }
        }
    }

    private fun stop() {
        mode = 3
        textView.text = "Послушайте вашу запись"
        btm.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        minus.visibility = View.VISIBLE
        plus.visibility = View.VISIBLE
        if (mediaRecorder != null) {
            mediaRecorder!!.stop()
        }
    }

    private fun playRec() {
        mode = 4
        btm.setImageResource(R.drawable.ic_baseline_pause_24)
        try {
            releasePlayer()
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(fileName)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                btm.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                mode = 5
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRec() {
        mode = 5
        btm.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    private fun resumeRec() {
        mode = 4
        btm.setImageResource(R.drawable.ic_baseline_pause_24)
        if (mediaPlayer != null) {
            mediaPlayer!!.start()
        }
    }

    private fun releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder!!.release()
            mediaRecorder = null
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
        clear()
        releasePlayer()
        releaseRecorder()
    }
}
package com.example.vkcup.news

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.vkcup.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news.*

class MainActivityNews : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        title = "News"
        val viewModel = ViewModelProvider(this)
            .get(SwipeRightViewModel::class.java)

        viewModel
            .modelStream
            .observe(this, {
                bindCard(it)
            })

        motionLayout.setTransitionListener(object : TransitionAdapter() {

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                Log.d("progress", motionLayout.progress.toString())
                when (currentId) {
                    R.id.offScreenPass,
                    R.id.offScreenLike -> {
                        motionLayout.setTransition(R.id.rest, R.id.like)
                        viewModel.swipe()
                        motionLayout.progress = 0f
                    }
                }
            }

        })

        likeButton.setOnClickListener {
            motionLayout.transitionToState(R.id.like)
        }

        passButton.setOnClickListener {
            motionLayout.transitionToState(R.id.pass)
        }
    }

    private fun bindCard(model: SwipeRightModel) {
        Picasso.get()
            .load(model.bottom.url)
            .tag(MainActivityNews::class.java)
            .into(image1)
        Picasso.get()
            .load(model.top.url)
            .tag(MainActivityNews::class.java)
            .into(image)
        text1.text = model.bottom.text
        text.text = model.top.text
    }

}

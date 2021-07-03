package com.example.vkcup.news;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SwipeRightViewModel: ViewModel() {

    private val stream = MutableLiveData<SwipeRightModel>()

    val modelStream: LiveData<SwipeRightModel>
        get() = stream

    private val data = ListNews.NEWS
    private var currentIndex = 0

    private val topCard
        get() = data[currentIndex % data.size]
    private val bottomCard
        get() = data[(currentIndex + 1) % data.size]

    init {
        updateStream()
    }

    fun swipe() {
        currentIndex += 1
        updateStream()
    }

    private fun updateStream() {
        stream.value = SwipeRightModel(
            top = topCard,
            bottom = bottomCard
        )
    }

}
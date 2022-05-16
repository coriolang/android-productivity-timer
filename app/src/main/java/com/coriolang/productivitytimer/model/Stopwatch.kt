package com.coriolang.productivitytimer.model

import kotlinx.coroutines.*

class Stopwatch(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private var minutes = 0
    private var seconds = 0

    suspend fun increment() {
        withContext(defaultDispatcher) {
            seconds++

            if (seconds == 60) {
                seconds = 0
                minutes++
            }

            delay(1000)
        }
    }
    
    fun reset() {
        minutes = 0
        seconds = 0
    }

    private fun getFormatted(number: Int): String {
        return if (number < 10) {
            "0$number"
        } else {
            "$number"
        }
    }

    override fun toString(): String {
        return "${getFormatted(minutes)}:${getFormatted(seconds)}"
    }
}
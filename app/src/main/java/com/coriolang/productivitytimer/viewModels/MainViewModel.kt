package com.coriolang.productivitytimer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.productivitytimer.model.Stopwatch
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    private val stopwatch = Stopwatch()

    private val _totalSeconds = MutableLiveData(stopwatch.totalSeconds)
    val totalSeconds: LiveData<Int> = _totalSeconds

    private var stopwatchJob: Job? = null

    fun startStopwatch() {
        if (stopwatchJob != null && stopwatchJob!!.isActive) {
            return
        }

        stopwatchJob = viewModelScope.launch {
            while (isActive) {
                stopwatch.increment()
                _totalSeconds.value = stopwatch.totalSeconds
            }
        }
    }

    fun resetStopwatch() {
        if (stopwatchJob == null) {
            return
        }

        stopwatchJob!!.cancel()
        stopwatch.reset()
        _totalSeconds.value = stopwatch.totalSeconds
    }

    fun getStopwatchString() = stopwatch.toString()

    fun setUpperLimit(upperLimit: String) {
        if (upperLimit == "") {
            stopwatch.upperLimit = -1
        } else {
            stopwatch.upperLimit = upperLimit.toInt()
        }
    }

    fun checkUpperLimit() =
        stopwatch.upperLimit != -1 && stopwatch.totalSeconds == stopwatch.upperLimit
}
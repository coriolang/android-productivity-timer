package com.coriolang.productivitytimer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.productivitytimer.model.Stopwatch
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    private val stopwatch = Stopwatch()

    private val _stopwatchString = MutableLiveData(stopwatch.toString())
    val stopwatchString: LiveData<String> = _stopwatchString

    private var stopwatchJob: Job? = null

    fun startStopwatch() {
        if (stopwatchJob != null && stopwatchJob!!.isActive) {
            return
        }

        stopwatchJob = viewModelScope.launch {
            while (isActive) {
                stopwatch.increment()
                _stopwatchString.value = stopwatch.toString()
            }
        }
    }

    fun resetStopwatch() {
        if (stopwatchJob == null) {
            return
        }

        stopwatchJob!!.cancel()
        stopwatch.reset()
        _stopwatchString.value = stopwatch.toString()
    }
}
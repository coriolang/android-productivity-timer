package com.coriolang.productivitytimer

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.coriolang.productivitytimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // try it with coroutines!!
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var minutes = 0
        var seconds = 0

        val handler = Handler(Looper.getMainLooper())
        val counter: Runnable = object : Runnable {

            override fun run() {
                seconds++
                if (seconds == 60) {
                    seconds = 0
                    minutes++
                }

                val timeString = "${getFormatted(minutes)}:${getFormatted(seconds)}"
                binding.textView.text = timeString

                handler.postDelayed(this, 1000)
            }
        }

        binding.startButton.setOnClickListener {
            if (!handler.hasCallbacks(counter)) {
                handler.postDelayed(counter, 1000)
            }
        }

        binding.resetButton.setOnClickListener {
            if (handler.hasCallbacks(counter)) {
                handler.removeCallbacks(counter)

                minutes = 0
                seconds = 0

                binding.textView.text = getString(R.string.zero_zero)
            }
        }
    }

    private fun getFormatted(number: Int): String {
        return if (number < 10) {
            "0$number"
        } else {
            "$number"
        }
    }
}
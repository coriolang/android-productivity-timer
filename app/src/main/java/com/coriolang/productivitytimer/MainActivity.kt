package com.coriolang.productivitytimer

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import com.coriolang.productivitytimer.databinding.ActivityMainBinding
import com.coriolang.productivitytimer.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private val colors: IntArray = intArrayOf(
        Color.RED,
        Color.BLUE,
        Color.CYAN,
        Color.GRAY,
        Color.GREEN,
        Color.DKGRAY,
        Color.YELLOW,
        Color.LTGRAY,
        Color.MAGENTA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            viewModel.startStopwatch()
            binding.progressBar.visibility = ProgressBar.VISIBLE
        }

        binding.resetButton.setOnClickListener {
            viewModel.resetStopwatch()
            binding.progressBar.visibility = ProgressBar.INVISIBLE
        }

        var lastColor = colors.random()

        viewModel.stopwatchString.observe(this) {
            binding.textView.text = it

            var currentColor = colors.random()
            while (currentColor == lastColor) {
                currentColor = colors.random()
            }
            lastColor = currentColor

            binding.progressBar.indeterminateTintList =
                ColorStateList.valueOf(currentColor)
        }
    }
}
package com.coriolang.productivitytimer

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import com.coriolang.productivitytimer.databinding.ActivityMainBinding
import com.coriolang.productivitytimer.databinding.TimeLimitDialogBinding
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
            binding.settingsButton.isEnabled = false
        }

        binding.resetButton.setOnClickListener {
            viewModel.resetStopwatch()

            binding.progressBar.visibility = ProgressBar.INVISIBLE
            binding.settingsButton.isEnabled = true
            binding.textView.setTextColor(Color.BLACK)
        }

        var upperLimit = ""

        binding.settingsButton.setOnClickListener {
            val dialogBinding = TimeLimitDialogBinding
                .inflate(layoutInflater, null, false)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.set_limit))
                .setView(dialogBinding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    upperLimit = dialogBinding.upperLimitEditText.text.toString()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        var lastColor = colors.random()

        viewModel.totalSeconds.observe(this) {
            binding.textView.text = viewModel.getStopwatchString()

            if (upperLimit != "" && it == upperLimit.toInt()) {
                binding.textView.setTextColor(Color.RED)
            }

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
package com.coriolang.productivitytimer

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.coriolang.productivitytimer.databinding.ActivityMainBinding
import com.coriolang.productivitytimer.databinding.TimeLimitDialogBinding
import com.coriolang.productivitytimer.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private val colors = intArrayOf(
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

        binding.settingsButton.setOnClickListener {
            val dialogBinding = TimeLimitDialogBinding
                .inflate(layoutInflater, null, false)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.set_limit))
                .setView(dialogBinding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val upperLimit = dialogBinding
                        .upperLimitEditText.text.toString()

                    viewModel.setUpperLimit(upperLimit)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        var lastColor = colors.random()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle(getString(R.string.stopwatch_notification))
            .setContentText(getString(R.string.time_exceeded))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        createNotificationChannel()

        viewModel.totalSeconds.observe(this) {
            binding.textView.text = viewModel.getStopwatchString()

            if (viewModel.checkUpperLimit()) {
                binding.textView.setTextColor(Color.RED)

                NotificationManagerCompat.from(this)
                    .notify(1, notificationBuilder.build())
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}
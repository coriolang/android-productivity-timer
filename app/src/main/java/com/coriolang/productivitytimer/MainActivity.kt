package com.coriolang.productivitytimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.coriolang.productivitytimer.databinding.ActivityMainBinding
import com.coriolang.productivitytimer.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            viewModel.startStopwatch()
        }

        binding.resetButton.setOnClickListener {
            viewModel.resetStopwatch()
        }

        viewModel.stopwatchString.observe(this) {
            binding.textView.text = it
        }
    }
}
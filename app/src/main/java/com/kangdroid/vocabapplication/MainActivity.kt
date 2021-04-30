package com.kangdroid.vocabapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kangdroid.vocabapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Activity Main Binding by lazy
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
    }
}
package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private val logTag: String = this::class.java.simpleName

    private val activityHomeBinding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityHomeBinding.root)

        // Setup bottomNavigationView
        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        activityHomeBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homePage -> {true}
                R.id.searchPage -> true
                R.id.learnPage -> {true}
                R.id.profilePage -> {true}
                else -> {
                    Log.w(logTag, "Unknown Navigation ID: ${it.itemId}")
                    false
                }
            }
        }
    }

    private fun commitFragment(targetFragment: Fragment, replace: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            if (replace) addToBackStack(null)
            replace(R.id.eachPageView, targetFragment)
            commit()
        }
    }
}
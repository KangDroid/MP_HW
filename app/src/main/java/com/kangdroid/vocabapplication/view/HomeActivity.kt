package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private val logTag: String = this::class.java.simpleName

    private val activityHomeBinding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    // Home Fragment
    @Inject
    lateinit var homeFragment: HomeFragment

    // Search Fragment
    @Inject
    lateinit var searchFragment: SearchFragment

    // Profile Fragment
    @Inject
    lateinit var profileFragment: ProfileFragment

    // Learn Fragment
    @Inject
    lateinit var learnFragment: LearnFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityHomeBinding.root)

        // Setup bottomNavigationView
        setupBottomNavigationView()

        // Setup initial page
        commitFragment(homeFragment)
    }

    private fun setupBottomNavigationView() {
        activityHomeBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homePage -> {
                    Log.d(logTag, "Initiating HomeFragment")
                    commitFragment(homeFragment)
                    true
                }
                R.id.searchPage -> {
                    Log.d(logTag, "Initiating SearchFragment")
                    commitFragment(searchFragment)
                    true
                }
                R.id.learnPage -> {
                    Log.d(logTag, "Initiating LearnFragment")
                    commitFragment(learnFragment)
                    true
                }
                R.id.profilePage -> {
                    Log.d(logTag, "Initiating ProfileFragment")
                    commitFragment(profileFragment)
                    true
                }
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
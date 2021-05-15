package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.ActivityHomeBinding
import com.kangdroid.vocabapplication.view.fragment.*
import com.kangdroid.vocabapplication.viewmodel.LearnPageRequest
import com.kangdroid.vocabapplication.viewmodel.LearnViewModel
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

    // MCQ Fragment
    @Inject
    lateinit var mcqFragment: MCQFragment

    // OE Fragment
    @Inject
    lateinit var oeFragment: OEFragment

    // listenMCQ Fragment
    @Inject
    lateinit var listenFragment: ListenFragment

    // ListenOE Fragment
    @Inject
    lateinit var listenOEFragment: ListenOEFragment

    // Learn View Model
    private val learnViewModel: LearnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityHomeBinding.root)

        // Setup bottomNavigationView
        setupBottomNavigationView()

        // Init observer
        initObserver()

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

    private fun commitFragmentWithAnimation(targetFragment: Fragment, replace: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            if (replace) addToBackStack(null)
            replace(R.id.eachPageView, targetFragment)
            commit()
        }
    }

    private fun initObserver() {
        learnViewModel.pageRequest.observe(this) {
            when (it) {
                LearnPageRequest.REQUEST_LEARN_MAIN -> commitFragmentWithAnimation(learnFragment)
                LearnPageRequest.REQUEST_MCQ -> commitFragmentWithAnimation(mcqFragment)
                LearnPageRequest.REQUEST_OE -> commitFragmentWithAnimation(oeFragment)
                LearnPageRequest.REQUEST_LISTEN_MCQ -> commitFragmentWithAnimation(listenFragment)
                LearnPageRequest.REQUEST_LISTEN_OE -> commitFragmentWithAnimation(listenOEFragment)
                else -> {}
            }
        }
    }
}
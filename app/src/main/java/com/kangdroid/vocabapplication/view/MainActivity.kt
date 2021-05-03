package com.kangdroid.vocabapplication.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// The View
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Activity Main Binding by lazy
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Login Fragment
    @Inject
    lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        commitFragment(loginFragment, false)
    }

    private fun commitFragment(targetFragment: Fragment, replace: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            if (replace) addToBackStack(null)
            replace(R.id.loginOrJoin, targetFragment)
            commit()
        }
    }
}
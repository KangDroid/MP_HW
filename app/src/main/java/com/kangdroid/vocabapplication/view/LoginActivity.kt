package com.kangdroid.vocabapplication.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.databinding.ActivityMainBinding
import com.kangdroid.vocabapplication.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// The View
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    // Activity Main Binding by lazy
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Login Fragment
    @Inject
    lateinit var loginFragment: LoginFragment

    // Register Fragment
    @Inject
    lateinit var registerFragment: RegisterFragment

    // Main View Model
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        // Set Observer
        setObservers()

        // Call View model to update data
        loginViewModel.requestDBCheck()
    }

    // Set Live Data observer for login view model
    private fun setObservers() {
        loginViewModel.registerNeeded.observe(this) {
            Log.d(this::class.java.simpleName, "Observed DatabaseEmptyLiveData, value: $it")
            if (!it) {
                // We have users. show Login page
                commitFragment(loginFragment, false)
            } else {
                // We do not have users. show register page
                commitFragment(registerFragment, false)
            }
        }
    }

    private fun commitFragment(targetFragment: Fragment, replace: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            if (replace) addToBackStack(null)
            replace(R.id.loginOrJoin, targetFragment)
            commit()
        }
    }
}
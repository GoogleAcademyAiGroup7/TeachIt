package com.swanky.teachit.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.swanky.teachit.R
import com.swanky.teachit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        setBottomNav()
    }


    private fun setBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set the BottomNavigationView with the NavController
        //NavigationUI.setupWithNavController(dataBinding.bottomNavigationView, navController)

        // Listen for back button presses to handle navigation properly
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = navController.currentDestination

                // Check if the current destination is HomeFragment
                if (currentFragment?.id == R.id.homeFragment) {
                    // If we're on HomeFragment, let the system handle back press normally
                    // We don't want to override the back press
                    isEnabled = false // Disable the callback to let the system handle it
                    onBackPressedDispatcher.onBackPressed() // Let the system handle the back press
                } else {
                    // If we're not on HomeFragment, navigate back to it using the NavController
                    if (!navController.popBackStack()) {
                        // If the back stack is empty, navigate to HomeFragment
                        navController.navigate(R.id.homeFragment)
                    }
                }
            }
        })
    }


}
package com.engineer.fred.easyfood.presentation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.engineer.fred.easyfood.R
import com.engineer.fred.easyfood.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get()= checkNotNull( _binding )

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme( R.style.Theme_EasyFood )
        _binding = ActivityMainBinding.inflate( layoutInflater )
        setContentView( binding.root )
        setUpBottomNavigation()

    }

    private fun setUpBottomNavigation() {
        val navContainer = supportFragmentManager.findFragmentById( R.id.fragment_host ) as NavHostFragment
        val navController = navContainer.navController
        binding.bottomNavigation.setupWithNavController( navController )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
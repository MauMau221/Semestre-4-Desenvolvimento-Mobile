package com.example.mobile.ui.theme.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobile.ui.theme.features.main.HomeFragment
import com.example.mobile.R
import com.example.mobile.ui.theme.features.main.ClientFragment
import com.example.mobile.ui.theme.features.main.OSFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_os -> loadFragment(OSFragment())
                R.id.nav_clients -> loadFragment(ClientFragment())
                R.id.nav_agenda -> loadFragment(OSFragment())
                R.id.nav_extra -> loadFragment(HomeFragment())
            }
            true
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.navigationView, fragment)
            .commit()
    }
}
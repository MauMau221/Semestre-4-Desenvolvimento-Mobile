package com.example.mobile.ui.theme.features.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobile.R
import com.example.mobile.databinding.ActivityHomeBinding // 1. Import do ViewBinding
import com.example.mobile.ui.theme.features.cliente.ClienteFragment
import com.example.mobile.ui.theme.features.os.OSFragment
// Adicione os imports que faltam para seus outros fragments
// import com.example.mobile.ui.theme.features.agenda.AgendaFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeFragment = HomeFragment()
    private val osFragment = OSFragment()
    private val clientFragment = ClienteFragment()

    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.navigationView, clientFragment, "3").hide(clientFragment)
                add(R.id.navigationView, osFragment, "2").hide(osFragment)
                add(R.id.navigationView, homeFragment, "1")
            }.commit()
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchFragment(homeFragment)
                R.id.nav_os -> switchFragment(osFragment)
                R.id.nav_clients -> switchFragment(clientFragment)
                // R.id.nav_agenda -> switchFragment(agendaFragment)
                // R.id.nav_extra -> switchFragment(extraFragment)
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }
}
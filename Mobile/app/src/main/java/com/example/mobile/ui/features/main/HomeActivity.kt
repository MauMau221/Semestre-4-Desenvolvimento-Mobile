package com.example.mobile.ui.theme.features.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobile.R
import com.example.mobile.databinding.ActivityHomeBinding // 1. Import do ViewBinding
import com.example.mobile.ui.theme.features.cliente.ClientFragment
import com.example.mobile.ui.theme.features.os.OSFragment
// Adicione os imports que faltam para seus outros fragments
// import com.example.mobile.ui.theme.features.agenda.AgendaFragment

class HomeActivity : AppCompatActivity() {

    // 2. Declaração do ViewBinding e das instâncias dos fragments
    private lateinit var binding: ActivityHomeBinding
    private val homeFragment = HomeFragment()
    private val osFragment = OSFragment()
    private val clientFragment = ClientFragment()
    // private val agendaFragment = AgendaFragment() // Exemplo
    // private val extraFragment = ExtraFragment()   // Exemplo

    // Variável para controlar o fragment ativo
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configuração do ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Adiciona os fragments ao container apenas na primeira vez
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.navigationView, clientFragment, "3").hide(clientFragment)
                add(R.id.navigationView, osFragment, "2").hide(osFragment)
                // O último adicionado sem hide() será o primeiro a ser exibido
                add(R.id.navigationView, homeFragment, "1")
            }.commit()
        }

        // 4. Configura o listener para o BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchFragment(homeFragment)
                R.id.nav_os -> switchFragment(osFragment)
                R.id.nav_clients -> switchFragment(clientFragment)
                // Associe os outros itens de menu aos seus fragments
                // R.id.nav_agenda -> switchFragment(agendaFragment)
                // R.id.nav_extra -> switchFragment(extraFragment)
            }
            true
        }
    }

    // 5. Função otimizada para trocar fragments usando show/hide
    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }
}
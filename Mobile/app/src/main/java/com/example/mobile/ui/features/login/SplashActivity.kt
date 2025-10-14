package com.example.mobile.ui.features.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.ui.theme.features.login.LoginActivity
import com.example.mobile.ui.theme.features.main.HomeActivity
import com.example.mobile.utils.PrefsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var prefs: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        prefs = PrefsHelper(this)

        // Inicia a verificação
        lifecycleScope.launch {
            // Um pequeno delay para a splash não piscar rápido demais
            delay(1000)

            val token = prefs.getToken()

            if (token.isNullOrBlank()) {
                // Se não há token, vá para o login
                goToLogin()
            } else {
                // Se há token, valide-o
                validateToken(token)
            }
        }
    }

    private fun validateToken(token: String) {
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(this@SplashActivity)
                // Formatar o token como "Bearer <token>"
                val response = api.checkToken("Bearer $token")

                if (response.isSuccessful) {
                    // Token válido, vá para a Home
                    goToHome()
                } else {
                    // Token inválido ou expirado, vá para o login
                    goToLogin()
                }
            } catch (e: Exception) {
                // Erro de rede, vá para o login como fallback
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Fecha a SplashActivity
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish() // Fecha a SplashActivity
    }
}
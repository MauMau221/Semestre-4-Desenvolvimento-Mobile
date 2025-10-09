package com.example.mobile.ui.theme.features.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.LoginRequest
import com.example.mobile.ui.theme.features.main.HomeActivity
import com.example.mobile.utils.PrefsHelper
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var prefs: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = PrefsHelper(this)
        val api = RetrofitClient.getInstance(this)

        val emailInput = findViewById<EditText>(R.id.emailEditText)
        val passwordInput = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            Log.d("LOGIN", "Email: $email | Password: $password")

            lifecycleScope.launch {
                try {
                    val response = api.login(LoginRequest(email, password))
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            prefs.saveToken(it.accessToken)
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        }
                    }else {
                        Toast.makeText(this@LoginActivity,"Credenciais Inválidas", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("LOGIN", "Erro: ${e.message}")
                }
            }
        }
    }
}
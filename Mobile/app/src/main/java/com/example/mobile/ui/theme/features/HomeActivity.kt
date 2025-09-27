package com.example.mobile.ui.theme.features

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.RetrofitClient
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userText = findViewById<TextView>(R.id.userText)
        val api = RetrofitClient.getInstance(this)

        lifecycleScope.launch {
            try {
                val response = api.getClientes()
                if (response.isSuccessful) {
                    val cliente = response.body()
                    userText.text = "Bem Vindo. ${cliente?.nome}"
                } else {
                    userText.text = "Erro: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("USER", "Erro: ${e.message}")
            }
        }
    }
}
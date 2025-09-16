package com.example.mobile

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.network.*
import com.example.mobile.network.models.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userText = findViewById<TextView>(R.id.userText)
        val api = RetrofitClient.getInstance(this)

        lifecycleScope.launch {
            try {
                val response = api.getUser()
                if (response.isSuccessful) {
                    val user = response.body()
                    userText.text = "Bem Vindo. ${user?.name}"
                } else {
                    userText.text = "Erro: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("USER", "Erro: ${e.message}")
            }
        }
    }
}
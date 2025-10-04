package com.example.mobile.ui.theme.features

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.RetrofitClient
import com.example.mobile.ui.theme.features.clientes.ClienteAdapter
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Referencia para o recyclerView no layout
        val recyclierView = findViewById<RecyclerView>(R.id.recyclerViewClientes)

        //Define como os itens serão organizados (Lista vertical)
        recyclierView.layoutManager = LinearLayoutManager(this)

        //Pega a instancia do serviço de API
        val api = RetrofitClient.getInstance(this)

        lifecycleScope.launch {
            try {
                val response = api.getClientes()
                if (response.isSuccessful) {
                    val clientes = response.body()

                    Log.d("HOME_ACTIVITY_DEBUG", "O corpo da resposta é nulo? ${clientes == null}")


                    // Acessa a lista de clientes DENTRO do objeto de resposta
                    val listaDeClientes = clientes?.data
                    Log.d("HOME_ACTIVITY_DEBUG", "A lista 'data' é nula? ${listaDeClientes == null}")
                    Log.d("HOME_ACTIVITY_DEBUG", "Tamanho da lista: ${listaDeClientes?.size}")

                    if (listaDeClientes?.isNotEmpty() == true) {
                        Log.d("HOME_ACTIVITY_DEBUG", "Primeiro cliente: ${listaDeClientes.firstOrNull()}")
                    }


                    if (!listaDeClientes.isNullOrEmpty()) {
                        //cria o adapter com lista e conecta no recyclerView
                        val adapter = ClienteAdapter(listaDeClientes)
                        recyclierView.adapter = adapter
                    }else {
                        Toast.makeText(this@HomeActivity, "Nenhum cliente encontrado", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(this@HomeActivity, "Erro ao carregar clientes: ${response.code()}",
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("HOME_ACTIVITY", "Falha na conexão/parsing: ${e.message}")
                    Toast.makeText(
                        this@HomeActivity,
                        "Falha na conexão com o servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
package com.example.mobile.ui.theme.features.cliente

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class ClienteFragment : Fragment() {

    private val ADD_CLIENT_REQUEST_CODE = 1

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_client, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewClientes)
        val fabAddClient = view.findViewById<View>(R.id.fab_add_client)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fabAddClient.setOnClickListener {
            val intent = Intent(requireContext(), AddClienteActivity::class.java)

            startActivityForResult(intent, ADD_CLIENT_REQUEST_CODE)
        }

        loadClients()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_CLIENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Atualizando a lista de clientes...", Toast.LENGTH_SHORT).show()
                loadClients()
            }
        }
    }

    private fun loadClients() {
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.getClientes()
                if (response.isSuccessful) {
                    val listaDeClientes = response.body()?.data
                    if (!listaDeClientes.isNullOrEmpty()) {
                        recyclerView.adapter = ClienteAdapter(listaDeClientes)
                    } else {
                        Toast.makeText(requireContext(), "Nenhum cliente encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro ao carregar clientes: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CLIENT_FRAGMENT_DEBUG", "Falha na conexão: ${e.message}")
            }
        }
    }
}
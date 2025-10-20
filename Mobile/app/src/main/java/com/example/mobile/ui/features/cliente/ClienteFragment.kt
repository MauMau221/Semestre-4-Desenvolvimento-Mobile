package com.example.mobile.ui.theme.features.cliente

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
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
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import com.example.mobile.data.remote.dtos.Cliente
import android.content.res.ColorStateList

class ClienteFragment : Fragment() {

    private val ADD_CLIENT_REQUEST_CODE = 1

    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var chipNome: Chip
    private lateinit var chipCpf: Chip
    private lateinit var chipModelo: Chip
    private lateinit var chipPlaca: Chip
    
    private var allClientes: List<Cliente> = emptyList()
    private var currentFilter = "NOME"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_client, container, false)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewClientes)
        etSearch = view.findViewById(R.id.et_search)
        chipNome = view.findViewById(R.id.chip_nome)
        chipCpf = view.findViewById(R.id.chip_cpf)
        chipModelo = view.findViewById(R.id.chip_modelo)
        chipPlaca = view.findViewById(R.id.chip_placa)
        val fabAddClient = view.findViewById<View>(R.id.fab_add_client)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Setup search and filters
        setupSearchAndFilters()
        
        // Setup FAB
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
    
    private fun setupSearchAndFilters() {
        // Setup search
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterAndDisplayClients()
            }
        })
        
        // Setup filter chips
        chipNome.setOnClickListener { setFilter("NOME") }
        chipCpf.setOnClickListener { setFilter("CPF") }
        chipModelo.setOnClickListener { setFilter("MODELO") }
        chipPlaca.setOnClickListener { setFilter("PLACA") }
    }
    
    private fun setFilter(filter: String) {
        currentFilter = filter
        
        // Update chip selection
        chipNome.isSelected = filter == "NOME"
        chipCpf.isSelected = filter == "CPF"
        chipModelo.isSelected = filter == "MODELO"
        chipPlaca.isSelected = filter == "PLACA"
        
        // Update chip colors
        updateChipColors()
        
        // Filter and display clients
        filterAndDisplayClients()
    }
    
    private fun updateChipColors() {
        val selectedColor = resources.getColor(R.color.chip_selected, null)
        val unselectedColor = resources.getColor(R.color.chip_unselected, null)
        
        chipNome.setChipBackgroundColor(ColorStateList.valueOf(if (chipNome.isSelected) selectedColor else unselectedColor))
        chipCpf.setChipBackgroundColor(ColorStateList.valueOf(if (chipCpf.isSelected) selectedColor else unselectedColor))
        chipModelo.setChipBackgroundColor(ColorStateList.valueOf(if (chipModelo.isSelected) selectedColor else unselectedColor))
        chipPlaca.setChipBackgroundColor(ColorStateList.valueOf(if (chipPlaca.isSelected) selectedColor else unselectedColor))
    }
    
    private fun filterAndDisplayClients() {
        val searchText = etSearch.text.toString().lowercase()
        
        val filteredList = if (searchText.isEmpty()) {
            allClientes
        } else {
            allClientes.filter { cliente ->
                when (currentFilter) {
                    "NOME" -> cliente.nome?.lowercase()?.contains(searchText) == true
                    "CPF" -> cliente.cpfCnpj?.lowercase()?.contains(searchText) == true
                    "MODELO" -> cliente.veiculos.any { it.modelo?.lowercase()?.contains(searchText) == true }
                    "PLACA" -> cliente.veiculos.any { it.placa?.lowercase()?.contains(searchText) == true }
                    else -> false
                }
            }
        }
        
        recyclerView.adapter = ClienteAdapter(
            filteredList,
            onItemClick = { cliente ->
                // Navigate to client details
                val intent = Intent(requireContext(), ClienteDetalhesActivity::class.java)
                intent.putExtra("cliente_id", cliente.id)
                startActivity(intent)
            },
            onItemLongClick = { view, cliente ->
                showOptionsDialog(cliente)
            }
        )
    }

    private fun loadClients() {
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.getClientes()
                if (response.isSuccessful) {
                    allClientes = response.body()?.data ?: emptyList()
                    filterAndDisplayClients()
                } else {
                    Toast.makeText(requireContext(), "Erro ao carregar clientes: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CLIENT_FRAGMENT_DEBUG", "Falha na conexão: ${e.message}")
            }
        }
    }

    private fun showOptionsDialog(cliente: Cliente) {
        val options = arrayOf("Editar", "Excluir")
        AlertDialog.Builder(requireContext())
            .setTitle(cliente.nome ?: "Cliente")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(requireContext(), AddClienteActivity::class.java)
                        intent.putExtra("cliente_id", cliente.id)
                        startActivityForResult(intent, ADD_CLIENT_REQUEST_CODE)
                    }
                    1 -> confirmDelete(cliente)
                }
            }
            .show()
    }

    private fun confirmDelete(cliente: Cliente) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir cliente")
            .setMessage("Tem certeza que deseja excluir ${cliente.nome}? Esta ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ -> deleteCliente(cliente.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteCliente(id: Int) {
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.deleteCliente(id)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Cliente excluído", Toast.LENGTH_SHORT).show()
                    loadClients()
                } else {
                    Toast.makeText(requireContext(), "Erro ao excluir: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
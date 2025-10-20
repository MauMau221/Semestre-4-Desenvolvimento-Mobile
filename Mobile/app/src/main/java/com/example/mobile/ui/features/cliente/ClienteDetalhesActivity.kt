package com.example.mobile.ui.theme.features.cliente

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.Cliente as ClienteDTO
import com.example.mobile.data.remote.dtos.VeiculoDTO
import kotlinx.coroutines.launch

class ClienteDetalhesActivity : AppCompatActivity() {
    
    private lateinit var tvClienteNome: TextView
    private lateinit var tvTelefone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvEndereco: TextView
    private lateinit var tvClienteDesde: TextView
    private lateinit var tvTotalOS: TextView
    private lateinit var tvConcluidas: TextView
    private lateinit var tvTotalPago: TextView
    private lateinit var tvVehiclesCount: TextView
    private lateinit var recyclerViewVeiculos: RecyclerView
    private lateinit var btnNovoVeiculo: com.google.android.material.button.MaterialButton
    
    private var clienteId: Int = -1
    private var clienteCarregado: ClienteDTO? = null
    
    private val EDIT_VEICULO_REQUEST_CODE = 101
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_detalhes)
        
        clienteId = intent.getIntExtra("cliente_id", -1)
        if (clienteId == -1) {
            Toast.makeText(this, "Cliente não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initViews()
        loadClienteData()
    }
    
    private fun initViews() {
        // Initialize views
        recyclerViewVeiculos = findViewById(R.id.recyclerViewVeiculos)
        recyclerViewVeiculos.layoutManager = LinearLayoutManager(this)

        btnNovoVeiculo = findViewById(R.id.btn_novo_veiculo)
        tvClienteNome = findViewById(R.id.tv_cliente_nome)
        tvTelefone = findViewById(R.id.tv_telefone)
        tvEmail = findViewById(R.id.tv_email)
        tvEndereco = findViewById(R.id.tv_endereco)
        tvClienteDesde = findViewById(R.id.tv_cliente_desde)
        tvTotalOS = findViewById(R.id.tv_total_os)
        tvConcluidas = findViewById(R.id.tv_concluidas)
        tvTotalPago = findViewById(R.id.tv_total_pago)
        tvVehiclesCount = findViewById(R.id.tv_vehicles_count)

        findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }

        btnNovoVeiculo.setOnClickListener {
            val intent = Intent(this, VeiculoFormActivity::class.java)
            intent.putExtra("cliente_id", clienteId)
            startActivityForResult(intent, EDIT_VEICULO_REQUEST_CODE)
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_VEICULO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadClienteData() // Reload client data to refresh vehicles
        }
    }
    
    private fun loadClienteData() {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.getCliente(clienteId)
                if (response.isSuccessful) {
                    response.body()?.let { cliente ->
                        clienteCarregado = cliente
                        updateClientUI(cliente)
                        loadVeiculos()
                    }
                } else {
                    Toast.makeText(this@ClienteDetalhesActivity, "Erro ao carregar cliente: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ClienteDetalhesActivity, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateClientUI(cliente: ClienteDTO) {
        tvClienteNome.text = cliente.nome ?: "Nome não disponível"
        tvTelefone.text = cliente.telefone ?: "Telefone não disponível"
        tvEmail.text = cliente.email ?: "Email não disponível"
        tvEndereco.text = cliente.endereco ?: "Endereço não disponível"
        tvClienteDesde.text = "Cliente desde ${cliente.createdAt?.substringBefore("T") ?: "N/A"}"

        // Mock data for stats
        tvTotalOS.text = "2"
        tvConcluidas.text = "3"
        tvTotalPago.text = "R$ 1.350"
    }
    
    private fun loadVeiculos() {
        if (clienteCarregado == null) {
            Toast.makeText(this, "Cliente não carregado", Toast.LENGTH_SHORT).show()
            return
        }
        
        val veiculos = clienteCarregado!!.veiculos
        tvVehiclesCount.text = "${veiculos.size} veículo${if (veiculos.size != 1) "s" else ""} encontrado${if (veiculos.size != 1) "s" else ""}"
        
        // Convert Veiculo to VeiculoDTO
        val veiculosDTO = veiculos.map { veiculo ->
            VeiculoDTO(
                id = veiculo.id,
                modelo = veiculo.modelo,
                placa = veiculo.placa,
                ano = veiculo.ano,
                marca = veiculo.marca,
                cor = veiculo.cor,
                clienteId = clienteId,
                proprietario = null,
                createdAt = null,
                updatedAt = null
            )
        }
        
        val veiculoAdapter = VeiculoAdapter(
            veiculosDTO,
            onEditarClick = { veiculo ->
                val intent = Intent(this, VeiculoFormActivity::class.java)
                intent.putExtra("veiculo_id", veiculo.id)
                intent.putExtra("cliente_id", clienteId)
                startActivityForResult(intent, EDIT_VEICULO_REQUEST_CODE)
            },
            onExcluirClick = { veiculo ->
                confirmDeleteVeiculo(veiculo)
            }
        )
        recyclerViewVeiculos.adapter = veiculoAdapter
    }
    
    private fun confirmDeleteVeiculo(veiculo: VeiculoDTO) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir o veículo ${veiculo.modelo} - ${veiculo.placa}?")
            .setPositiveButton("Excluir") { _, _ -> deleteVeiculo(veiculo.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun deleteVeiculo(veiculoId: Int) {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.deleteVeiculo(veiculoId)
                if (response.isSuccessful) {
                    Toast.makeText(this@ClienteDetalhesActivity, "Veículo excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    loadClienteData() // Reload client data to refresh vehicles
                } else {
                    Toast.makeText(this@ClienteDetalhesActivity, "Erro ao excluir veículo: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ClienteDetalhesActivity, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
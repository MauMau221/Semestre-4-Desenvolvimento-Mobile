package com.example.mobile.ui.theme.features.cliente

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.NewVeiculoRequest
import com.example.mobile.data.remote.dtos.VeiculoDTO
import com.example.mobile.data.remote.dtos.Cliente as ClienteDTO
import kotlinx.coroutines.launch

class VeiculoFormActivity : AppCompatActivity() {
    
    private var veiculoId: Int? = null
    private var clienteId: Int = -1
    
    private lateinit var etPlaca: EditText
    private lateinit var etMarca: EditText
    private lateinit var etModelo: EditText
    private lateinit var etAno: EditText
    private lateinit var etCor: EditText
    private lateinit var etCliente: AutoCompleteTextView
    private lateinit var btnCadastrar: com.google.android.material.button.MaterialButton
    private lateinit var btnCancelar: com.google.android.material.button.MaterialButton
    
    private var clientes: List<ClienteDTO> = emptyList()
    private var selectedClienteId: Int = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veiculo_form)
        
        veiculoId = intent.getIntExtra("veiculo_id", -1).takeIf { it != -1 }
        clienteId = intent.getIntExtra("cliente_id", -1)
        
        if (clienteId == -1 && veiculoId == null) {
            Toast.makeText(this, "ID do cliente não fornecido.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initViews()
        setupClienteDropdown()
        setupForm()
        
        if (veiculoId != null) {
            title = "EDITAR VEÍCULO"
            carregarDadosVeiculo()
        } else {
            title = "CADASTRAR VEÍCULO"
        }
    }
    
    private fun initViews() {
        etPlaca = findViewById(R.id.et_placa)
        etMarca = findViewById(R.id.et_marca)
        etModelo = findViewById(R.id.et_modelo)
        etAno = findViewById(R.id.et_ano)
        etCor = findViewById(R.id.et_cor)
        etCliente = findViewById(R.id.et_cliente)
        btnCadastrar = findViewById(R.id.btn_cadastrar)
        btnCancelar = findViewById(R.id.btn_cancelar)
        
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }
    }
    
    private fun setupClienteDropdown() {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.getClientes()
                if (response.isSuccessful) {
                    clientes = response.body()?.data ?: emptyList()
                    setupClienteAdapter()
                } else {
                    Toast.makeText(this@VeiculoFormActivity, "Erro ao carregar clientes: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@VeiculoFormActivity, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupClienteAdapter() {
        val clienteNames = clientes.map { it.nome ?: "Cliente sem nome" }
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, clienteNames)
        etCliente.setAdapter(adapter)
        etCliente.threshold = 0
        
        // Se veiculoId não é null, significa que estamos editando e clienteId já está definido
        if (veiculoId != null && clienteId != -1) {
            val cliente = clientes.find { it.id == clienteId }
            if (cliente != null) {
                etCliente.setText(cliente.nome, false)
                selectedClienteId = cliente.id
            }
        }
        
        etCliente.setOnItemClickListener { _, _, position, _ ->
            selectedClienteId = clientes[position].id
        }
    }
    
    private fun setupForm() {
        btnCadastrar.setOnClickListener {
            val placa = etPlaca.text.toString().trim()
            val marca = etMarca.text.toString().trim()
            val modelo = etModelo.text.toString().trim()
            val ano = etAno.text.toString().trim()
            val cor = etCor.text.toString().trim()
            val cliente = etCliente.text.toString().trim()
            
            if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || ano.isEmpty() || cor.isEmpty()) {
                Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (veiculoId == null && (cliente.isEmpty() || selectedClienteId == -1)) {
                Toast.makeText(this, "Selecione um cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val anoInt = ano.toIntOrNull()
            if (anoInt == null || anoInt < 1900 || anoInt > 2030) {
                Toast.makeText(this, "Ano inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val request = NewVeiculoRequest(
                clienteId = if (veiculoId != null) clienteId else selectedClienteId,
                modelo = modelo,
                placa = placa,
                ano = anoInt,
                marca = marca,
                cor = cor,
                proprietario = null
            )
            
            salvarVeiculo(request)
        }
        
        btnCancelar.setOnClickListener {
            finish()
        }
    }
    
    private fun salvarVeiculo(request: NewVeiculoRequest) {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = if (veiculoId != null) {
                    api.updateVeiculo(veiculoId!!, request)
                } else {
                    api.createVeiculo(request)
                }
                
                if (response.isSuccessful) {
                    Toast.makeText(this@VeiculoFormActivity, "Veículo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@VeiculoFormActivity, "Erro ao salvar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@VeiculoFormActivity, "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun carregarDadosVeiculo() {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.getVeiculo(veiculoId!!)
                if (response.isSuccessful) {
                    response.body()?.let { veiculo ->
                        etPlaca.setText(veiculo.placa ?: "")
                        etMarca.setText(veiculo.marca ?: "")
                        etModelo.setText(veiculo.modelo ?: "")
                        etAno.setText(veiculo.ano?.toString() ?: "")
                        etCor.setText(veiculo.cor ?: "")
                        clienteId = veiculo.clienteId
                    }
                } else {
                    Toast.makeText(this@VeiculoFormActivity, "Erro ao carregar veículo: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@VeiculoFormActivity, "Falha ao carregar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
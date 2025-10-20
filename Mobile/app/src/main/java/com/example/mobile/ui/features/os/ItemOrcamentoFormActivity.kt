package com.example.mobile.ui.theme.features.os

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.NewItemOrcamentoRequest
import com.example.mobile.data.remote.dtos.ItemOrcamentoDTO
import kotlinx.coroutines.launch

class ItemOrcamentoFormActivity : AppCompatActivity() {
    
    private var itemId: Int? = null
    private var osId: Int = -1
    
    private lateinit var etDescricao: EditText
    private lateinit var etTipo: AutoCompleteTextView
    private lateinit var etQuantidade: EditText
    private lateinit var etPrecoUnitario: EditText
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_orcamento_form)
        
        itemId = intent.getIntExtra("item_id", -1).takeIf { it != -1 }
        osId = intent.getIntExtra("os_id", -1)
        
        if (osId == -1) {
            Toast.makeText(this, "OS inválida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initViews()
        setupTipoDropdown()
        setupForm()
        
        if (itemId != null) {
            title = "Editar Item"
            loadItemData()
        } else {
            title = "Novo Item"
        }
    }
    
    private fun initViews() {
        etDescricao = findViewById(R.id.et_descricao)
        etTipo = findViewById(R.id.et_tipo)
        etQuantidade = findViewById(R.id.et_quantidade)
        etPrecoUnitario = findViewById(R.id.et_preco_unitario)
    }
    
    private fun setupTipoDropdown() {
        val tipoOptions = listOf("Peça", "Serviço")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipoOptions)
        etTipo.setAdapter(adapter)
        etTipo.threshold = 0
    }
    
    private fun setupForm() {
        findViewById<android.view.View>(R.id.btn_salvar).setOnClickListener {
            val descricao = etDescricao.text.toString().trim()
            val tipo = etTipo.text.toString().trim()
            val quantidade = etQuantidade.text.toString().trim()
            val precoUnitario = etPrecoUnitario.text.toString().trim()
            
            if (descricao.isEmpty()) {
                Toast.makeText(this, "Descrição é obrigatória", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (tipo.isEmpty() || tipo == "Selecione o tipo") {
                Toast.makeText(this, "Selecione um tipo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (quantidade.isEmpty()) {
                Toast.makeText(this, "Quantidade é obrigatória", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (precoUnitario.isEmpty()) {
                Toast.makeText(this, "Preço unitário é obrigatório", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val quantidadeInt = quantidade.toIntOrNull()
            val precoDouble = precoUnitario.toDoubleOrNull()
            
            if (quantidadeInt == null || quantidadeInt <= 0) {
                Toast.makeText(this, "Quantidade deve ser um número positivo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (precoDouble == null || precoDouble <= 0) {
                Toast.makeText(this, "Preço deve ser um número positivo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val tipoValue = if (tipo == "Peça") "PECA" else "SERVICO"
            
            val request = NewItemOrcamentoRequest(
                descricao = descricao,
                tipo = tipoValue,
                quantidade = quantidadeInt,
                precoUnitario = precoDouble,
                osId = osId
            )
            
            saveItem(request)
        }
    }
    
    private fun saveItem(request: NewItemOrcamentoRequest) {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = if (itemId != null) {
                    api.updateItemOrcamento(itemId!!, request)
                } else {
                    api.createItemOrcamento(request)
                }
                
                if (response.isSuccessful) {
                    Toast.makeText(this@ItemOrcamentoFormActivity, "Item salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@ItemOrcamentoFormActivity, "Erro ao salvar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ItemOrcamentoFormActivity, "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadItemData() {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.getItemOrcamento(itemId!!)
                if (response.isSuccessful) {
                    response.body()?.let { item ->
                        etDescricao.setText(item.descricao)
                        etTipo.setText(if (item.tipo == "PECA") "Peça" else "Serviço")
                        etQuantidade.setText(item.quantidade.toString())
                        etPrecoUnitario.setText(item.precoUnitario.toString())
                    }
                } else {
                    Toast.makeText(this@ItemOrcamentoFormActivity, "Erro ao carregar item: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ItemOrcamentoFormActivity, "Falha ao carregar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

package com.example.mobile.ui.theme.features.os

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.ItemVistoriaDTO
import com.example.mobile.data.remote.dtos.NewItemVistoriaRequest
import kotlinx.coroutines.launch

class ItemVistoriaFormActivity : AppCompatActivity() {
    
    private var itemId: Int? = null
    private var osId: Int = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_vistoria_form)
        
        itemId = intent.getIntExtra("item_id", -1).takeIf { it != -1 }
        osId = intent.getIntExtra("os_id", -1)
        
        if (osId == -1) {
            Toast.makeText(this, "OS inválida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        setupStatusDropdown()
        setupForm()
        
        if (itemId != null) {
            title = "Editar Item"
            loadItemData()
        } else {
            title = "Novo Item"
        }
    }
    
    private fun setupStatusDropdown() {
        val statusOptions = listOf("Crítico", "Atenção", "OK")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusOptions)
        val autoComplete = findViewById<AutoCompleteTextView>(R.id.et_status)
        autoComplete.setAdapter(adapter)
        autoComplete.threshold = 0 // Show dropdown immediately on click
        
        // Set default selection
        if (autoComplete.text.toString() == "Selecione o status") {
            autoComplete.setText("", false)
        }
    }
    
    private fun setupForm() {
        findViewById<android.view.View>(R.id.btn_salvar).setOnClickListener {
            val titulo = findViewById<EditText>(R.id.et_titulo).text.toString().trim()
            val descricao = findViewById<EditText>(R.id.et_descricao).text.toString().trim()
            val status = findViewById<AutoCompleteTextView>(R.id.et_status).text.toString().trim()
            
            if (titulo.isEmpty()) {
                Toast.makeText(this, "Título é obrigatório", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (status.isEmpty() || status == "Selecione o status") {
                Toast.makeText(this, "Selecione um status", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val request = NewItemVistoriaRequest(
                titulo = titulo,
                descricao = if (descricao.isEmpty()) null else descricao,
                status = status,
                osId = osId
            )
            
            salvarItem(request)
        }
    }
    
    private fun loadItemData() {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.getItemVistoria(itemId!!)
                if (response.isSuccessful) {
                    response.body()?.let { item ->
                        findViewById<EditText>(R.id.et_titulo).setText(item.titulo)
                        findViewById<EditText>(R.id.et_descricao).setText(item.descricao ?: "")
                        findViewById<AutoCompleteTextView>(R.id.et_status).setText(item.status)
                    }
                } else {
                    Toast.makeText(this@ItemVistoriaFormActivity, "Erro ao carregar item: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ItemVistoriaFormActivity, "Falha ao carregar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun salvarItem(request: NewItemVistoriaRequest) {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = if (itemId != null) {
                    api.updateItemVistoria(itemId!!, request)
                } else {
                    api.createItemVistoria(request)
                }
                
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@ItemVistoriaFormActivity, "Erro ao salvar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ItemVistoriaFormActivity, "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

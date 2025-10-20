package com.example.mobile.ui.theme.features.os

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.NewOrdemServicoRequest
import kotlinx.coroutines.launch

class AddOSActivity : AppCompatActivity() {
    private val ADD_OS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_os)

        val clienteIdText = findViewById<EditText>(R.id.et_cliente_id)
        val descricaoText = findViewById<EditText>(R.id.et_descricao)
        val statusText = findViewById<EditText>(R.id.et_status)
        val valorText = findViewById<EditText>(R.id.et_valor)
        val btnSalvar = findViewById<Button>(R.id.btn_salvar_os)

        val osId = intent.getIntExtra("os_id", -1)
        if (osId != -1) {
            // Modo edição: buscar dados e preencher
            val api = RetrofitClient.getInstance(this)
            lifecycleScope.launch {
                try {
                    val resp = api.getOrdemServico(osId)
                    if (resp.isSuccessful) {
                        resp.body()?.let { os ->
                            clienteIdText.setText(os.clienteId.toString())
                            descricaoText.setText(os.descricao ?: "")
                            statusText.setText(os.status ?: "")
                            valorText.setText(os.valorTotal?.toString() ?: "")
                        }
                    }
                } catch (_: Exception) {}
            }
        }

        btnSalvar.setOnClickListener {
            val clienteId = clienteIdText.text.toString().toIntOrNull()
            if (clienteId == null) {
                clienteIdText.error = "Informe o cliente_id"
                return@setOnClickListener
            }

            val request = NewOrdemServicoRequest(
                cliente_id = clienteId,
                descricao = descricaoText.text.toString().takeIf { it.isNotEmpty() },
                status = statusText.text.toString().takeIf { it.isNotEmpty() },
                valor_total = valorText.text.toString().toDoubleOrNull()
            )

            if (osId == -1) {
                createOSOnApi(request)
            } else {
                updateOSOnApi(osId, request)
            }
        }
    }

    private fun createOSOnApi(request: NewOrdemServicoRequest) {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.createOrdemServico(request)
                if (response.isSuccessful) {
                    Toast.makeText(this@AddOSActivity, "OS criada!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@AddOSActivity, "Erro ao criar OS: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddOSActivity, "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateOSOnApi(id: Int, request: NewOrdemServicoRequest) {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.updateOrdemServico(id, request)
                if (response.isSuccessful) {
                    Toast.makeText(this@AddOSActivity, "OS atualizada!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@AddOSActivity, "Erro ao atualizar OS: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddOSActivity, "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



package com.example.mobile.ui.theme.features.cliente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import android.app.Activity
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.NewClienteRequest
import kotlinx.coroutines.launch

class AddClienteActivity : AppCompatActivity() {

    // Facilitar a filtragem no Logcat
    private val TAG = "AddClientActivity_DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        Log.d(TAG, "Activity criada. Referenciando as views...")

        val editTextName = findViewById<EditText>(R.id.et_client_name)
        val editTextCpfCnpj = findViewById<EditText>(R.id.et_client_cpf_cnpj)
        val editTextEmail = findViewById<EditText>(R.id.et_client_email)
        val editTextTelefone = findViewById<EditText>(R.id.et_client_telefone)
        val editTextEndereco = findViewById<EditText>(R.id.et_client_endereco)
        val saveButton = findViewById<Button>(R.id.btn_save_client)

        saveButton.setOnClickListener {
            Log.d(TAG, "Botão Salvar CLICADO!")

            val clientName = editTextName.text.toString().trim()
            val clientCpfCnpj = editTextCpfCnpj.text.toString().trim()
            val clientEmail = editTextEmail.text.toString().trim()
            val clientTelefone = editTextTelefone.text.toString().trim()
            val clientEndereco = editTextEndereco.text.toString().trim()

            Log.d(TAG, "Dados lidos -> Nome: '$clientName', CPF: '$clientCpfCnpj'")

            if (clientName.isEmpty()) {
                Log.w(TAG, "VALIDAÇÃO FALHOU: O nome está vazio.")
                editTextName.error = "O nome é obrigatório"
                return@setOnClickListener
            }
            if (clientCpfCnpj.isEmpty()) {
                Log.w(TAG, "VALIDAÇÃO FALHOU: O CPF/CNPJ está vazio.")
                editTextCpfCnpj.error = "O CPF/CNPJ é obrigatório"
                return@setOnClickListener
            }

            Log.d(TAG, "Validação passou com sucesso.")

            val newClient = NewClienteRequest(
                nome = clientName,
                cpf_cnpj = clientCpfCnpj,
                email = clientEmail.takeIf { it.isNotEmpty() },
                telefone = clientTelefone.takeIf { it.isNotEmpty() },
                endereco = clientEndereco.takeIf { it.isNotEmpty() }
            )

            Log.d(TAG, "Objeto a ser enviado para a API: $newClient")

            createClientOnApi(newClient)
        }
    }

    private fun createClientOnApi(newClient: NewClienteRequest) {
        Log.d(TAG, "Iniciando a chamada de rede com lifecycleScope...")

        val api = RetrofitClient.getInstance(this)

        lifecycleScope.launch {
            try {
                Log.d(TAG, "Executando api.createCliente(newClient)...")
                val response = api.createCliente(newClient)

                if (response.isSuccessful) {
                    Log.i(TAG, "SUCESSO! Resposta da API foi bem-sucedida.")
                    Toast.makeText(this@AddClienteActivity, "Cliente salvo com sucesso!", Toast.LENGTH_LONG).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "ERRO DA API! Código: ${response.code()}. Corpo do erro: $errorBody")
                    Toast.makeText(this@AddClienteActivity, "Erro ao salvar. Verifique os dados.", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Log.e(TAG, "ERRO DE CONEXÃO OU CÓDIGO! A chamada falhou.", e) // O 'e' no final imprime o erro completo
                Toast.makeText(this@AddClienteActivity, "Falha na conexão com o servidor.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
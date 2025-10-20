package com.example.mobile.ui.theme.features.os

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.OrdemServicoDTO
import kotlinx.coroutines.launch

class OSDetalhesActivity : AppCompatActivity() {
    
    private var osId: Int = -1
    private var currentStage = "RESUMO"
    
    private lateinit var stageResumo: LinearLayout
    private lateinit var stageVistoria: LinearLayout
    private lateinit var stageOrcamento: LinearLayout
    private lateinit var stageServicos: LinearLayout
    private lateinit var stageEntrega: LinearLayout
    private lateinit var stagePagamento: LinearLayout
    
    private lateinit var tvOSNumber: TextView
    private lateinit var tvClienteVeiculo: TextView
    private lateinit var tvOSNumero: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvDataEntrada: TextView
    private lateinit var tvPrevisaoEntrega: TextView
    private lateinit var tvClienteNome: TextView
    private lateinit var tvVeiculoInfo: TextView
    private lateinit var tvResponsavel: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_os_detalhes)
        
        osId = intent.getIntExtra("os_id", -1)
        if (osId == -1) {
            Toast.makeText(this, "OS não encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        initViews()
        setupStages()
        loadOSData()
    }
    
    private fun initViews() {
        stageResumo = findViewById(R.id.stage_resumo)
        stageVistoria = findViewById(R.id.stage_vistoria)
        stageOrcamento = findViewById(R.id.stage_orcamento)
        stageServicos = findViewById(R.id.stage_servicos)
        stageEntrega = findViewById(R.id.stage_entrega)
        stagePagamento = findViewById(R.id.stage_pagamento)
        
        tvOSNumber = findViewById(R.id.tv_os_number)
        tvClienteVeiculo = findViewById(R.id.tv_cliente_veiculo)
        tvOSNumero = findViewById(R.id.tv_os_numero)
        tvStatus = findViewById(R.id.tv_status)
        tvDataEntrada = findViewById(R.id.tv_data_entrada)
        tvPrevisaoEntrega = findViewById(R.id.tv_previsao_entrega)
        tvClienteNome = findViewById(R.id.tv_cliente_nome)
        tvVeiculoInfo = findViewById(R.id.tv_veiculo_info)
        tvResponsavel = findViewById(R.id.tv_responsavel)
        
        findViewById<View>(R.id.iv_back).setOnClickListener { finish() }
    }
    
    private fun setupStages() {
        stageResumo.setOnClickListener { setStage("RESUMO") }
        stageVistoria.setOnClickListener { setStage("VISTORIA") }
        stageOrcamento.setOnClickListener { setStage("ORCAMENTO") }
        stageServicos.setOnClickListener { setStage("SERVICOS") }
        stageEntrega.setOnClickListener { setStage("ENTREGA") }
        stagePagamento.setOnClickListener { setStage("PAGAMENTO") }
        
        // Set initial stage
        setStage("RESUMO")
    }
    
    private fun setStage(stage: String) {
        currentStage = stage
        
        // Reset all stages
        resetStageAppearance(stageResumo)
        resetStageAppearance(stageVistoria)
        resetStageAppearance(stageOrcamento)
        resetStageAppearance(stageServicos)
        resetStageAppearance(stageEntrega)
        resetStageAppearance(stagePagamento)
        
        // Set selected stage
        when (stage) {
            "RESUMO" -> setSelectedStageAppearance(stageResumo)
            "VISTORIA" -> setSelectedStageAppearance(stageVistoria)
            "ORCAMENTO" -> setSelectedStageAppearance(stageOrcamento)
            "SERVICOS" -> setSelectedStageAppearance(stageServicos)
            "ENTREGA" -> setSelectedStageAppearance(stageEntrega)
            "PAGAMENTO" -> setSelectedStageAppearance(stagePagamento)
        }
        
        // Update content based on stage
        updateContentForStage(stage)
    }
    
    private fun resetStageAppearance(stage: LinearLayout) {
        stage.setBackgroundResource(R.drawable.stage_unselected)
        val textView = stage.getChildAt(1) as TextView
        val imageView = stage.getChildAt(0) as android.widget.ImageView
        textView.setTextColor(resources.getColor(R.color.red_primary, null))
        imageView.setColorFilter(resources.getColor(R.color.red_primary, null))
    }
    
    private fun setSelectedStageAppearance(stage: LinearLayout) {
        stage.setBackgroundResource(R.drawable.stage_selected)
        val textView = stage.getChildAt(1) as TextView
        val imageView = stage.getChildAt(0) as android.widget.ImageView
        textView.setTextColor(resources.getColor(android.R.color.white, null))
        imageView.setColorFilter(resources.getColor(android.R.color.white, null))
    }
    
    private fun updateContentForStage(stage: String) {
        val fragmentContainer = findViewById<android.widget.FrameLayout>(R.id.fragment_container)
        val contentScroll = findViewById<android.widget.ScrollView>(R.id.content_scroll)
        
        when (stage) {
            "RESUMO" -> {
                // Show summary content (already visible)
                contentScroll.visibility = android.view.View.VISIBLE
            }
            "VISTORIA" -> {
                contentScroll.visibility = android.view.View.GONE
                loadVistoriaContent()
            }
            "ORCAMENTO" -> {
                contentScroll.visibility = android.view.View.GONE
                loadOrcamentoContent()
            }
            "SERVICOS" -> {
                contentScroll.visibility = android.view.View.GONE
                loadServicosContent()
            }
            "ENTREGA" -> {
                contentScroll.visibility = android.view.View.GONE
                loadEntregaContent()
            }
            "PAGAMENTO" -> {
                contentScroll.visibility = android.view.View.GONE
                loadPagamentoContent()
            }
        }
    }
    
    private fun loadVistoriaContent() {
        val fragment = VistoriaFragment.newInstance(osId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    private fun loadOrcamentoContent() {
        val fragment = OrcamentoFragment.newInstance(osId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    private fun loadServicosContent() {
        val fragment = ServicosFragment.newInstance(osId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    private fun loadEntregaContent() {
        val fragment = EntregaFragment.newInstance(osId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    private fun loadPagamentoContent() {
        val fragment = PagamentoFragment.newInstance(osId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    private fun loadOSData() {
        val api = RetrofitClient.getInstance(this)
        lifecycleScope.launch {
            try {
                val response = api.getOrdemServico(osId)
                if (response.isSuccessful) {
                    response.body()?.let { os ->
                        displayOSInfo(os)
                    }
                } else {
                    Toast.makeText(this@OSDetalhesActivity, "Erro ao carregar OS: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OSDetalhesActivity, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun displayOSInfo(os: OrdemServicoDTO) {
        tvOSNumber.text = "OS-2025-${String.format("%03d", os.id)}"
        tvOSNumero.text = "OS-2025-${String.format("%03d", os.id)}"
        tvClienteVeiculo.text = "Cliente #${os.clienteId} - Veículo ABC-1234"
        tvClienteNome.text = "Cliente #${os.clienteId}"
        tvVeiculoInfo.text = "Veículo - Placa ABC-1234"
        tvResponsavel.text = "Carlos Mecânico"
        
        // Status
        val status = os.status?.uppercase() ?: "PENDENTE"
        tvStatus.text = status
        
        // Status background
        when (status) {
            "PENDENTE" -> tvStatus.setBackgroundResource(R.drawable.status_chip_pendente)
            "EM ANDAMENTO", "ANDAMENTO" -> tvStatus.setBackgroundResource(R.drawable.status_chip_andamento)
            "CONCLUIDO", "CONCLUÍDO" -> tvStatus.setBackgroundResource(R.drawable.status_chip_concluido)
            "CANCELADO" -> tvStatus.setBackgroundResource(R.drawable.status_chip_cancelado)
            else -> tvStatus.setBackgroundResource(R.drawable.status_chip_pendente)
        }
        
        // Dates
        tvDataEntrada.text = os.createdAt?.let { 
            try {
                val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val date = inputFormat.parse(it)
                outputFormat.format(date ?: java.util.Date())
            } catch (e: Exception) {
                "Data não disponível"
            }
        } ?: "Data não disponível"
        
        tvPrevisaoEntrega.text = "01/09/2025" // Mock data
    }
}

package com.example.mobile.ui.theme.features.os

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.dtos.OrdemServicoDTO
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class OSAdapter(
    private val ordens: List<OrdemServicoDTO>,
    private val onItemClick: (OrdemServicoDTO) -> Unit,
    private val onItemLongClick: (View, OrdemServicoDTO) -> Unit,
    private val onIniciarClick: (OrdemServicoDTO) -> Unit,
    private val onEditarClick: (OrdemServicoDTO) -> Unit,
    private val onCancelarClick: (OrdemServicoDTO) -> Unit
) : RecyclerView.Adapter<OSAdapter.OSViewHolder>() {

    class OSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val statusBar: View = itemView.findViewById(R.id.status_bar)
        private val idText: TextView = itemView.findViewById(R.id.tv_os_id)
        private val statusChip: TextView = itemView.findViewById(R.id.tv_status_chip)
        private val clienteNome: TextView = itemView.findViewById(R.id.tv_cliente_nome)
        private val veiculo: TextView = itemView.findViewById(R.id.tv_veiculo)
        private val servicos: TextView = itemView.findViewById(R.id.tv_servicos)
        private val data: TextView = itemView.findViewById(R.id.tv_data)
        private val total: TextView = itemView.findViewById(R.id.tv_total)
        private val btnIniciar: MaterialButton = itemView.findViewById(R.id.btn_iniciar)
        private val btnEditar: MaterialButton = itemView.findViewById(R.id.btn_editar)
        private val btnCancelar: MaterialButton = itemView.findViewById(R.id.btn_cancelar)

        fun bind(os: OrdemServicoDTO, onIniciar: (OrdemServicoDTO) -> Unit, onEditar: (OrdemServicoDTO) -> Unit, onCancelar: (OrdemServicoDTO) -> Unit) {
            // OS ID
            idText.text = "OS-2025-${String.format("%03d", os.id)}"
            
            // Status
            val status = os.status?.uppercase() ?: "PENDENTE"
            statusChip.text = status
            
            // Status bar color and chip background
            when (status) {
                "PENDENTE" -> {
                    statusBar.setBackgroundColor(itemView.context.getColor(R.color.status_pendente))
                    statusChip.setBackgroundResource(R.drawable.status_chip_pendente)
                }
                "EM ANDAMENTO", "ANDAMENTO" -> {
                    statusBar.setBackgroundColor(itemView.context.getColor(R.color.status_andamento))
                    statusChip.setBackgroundResource(R.drawable.status_chip_andamento)
                }
                "CONCLUIDO", "CONCLUÍDO" -> {
                    statusBar.setBackgroundColor(itemView.context.getColor(R.color.status_concluido))
                    statusChip.setBackgroundResource(R.drawable.status_chip_concluido)
                }
                "CANCELADO" -> {
                    statusBar.setBackgroundColor(itemView.context.getColor(R.color.status_cancelado))
                    statusChip.setBackgroundResource(R.drawable.status_chip_cancelado)
                }
                else -> {
                    statusBar.setBackgroundColor(itemView.context.getColor(R.color.gray_text))
                    statusChip.setBackgroundResource(R.drawable.status_chip_pendente)
                }
            }
            
            // Client info (mock data for now)
            clienteNome.text = "Cliente #${os.clienteId}"
            veiculo.text = "Veículo - Placa ABC-1234"
            
            // Services (mock data)
            servicos.text = "• ${os.descricao ?: "Serviço não especificado"}\n• +2 mais..."
            
            // Date
            data.text = os.createdAt?.let { 
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = inputFormat.parse(it)
                    outputFormat.format(date ?: Date())
                } catch (e: Exception) {
                    "Data não disponível"
                }
            } ?: "Data não disponível"
            
            // Total
            total.text = os.valorTotal?.let { "R$ %.2f".format(it).replace(".", ",") } ?: "R$ 0,00"
            
            // Button states based on status
            when (status) {
                "PENDENTE" -> {
                    btnIniciar.isEnabled = true
                    btnIniciar.text = "Iniciar"
                    btnCancelar.isEnabled = true
                    btnCancelar.text = "Cancelar"
                }
                "EM ANDAMENTO", "ANDAMENTO" -> {
                    btnIniciar.isEnabled = false
                    btnIniciar.text = "Em Andamento"
                    btnCancelar.isEnabled = true
                    btnCancelar.text = "Cancelar"
                }
                "CONCLUIDO", "CONCLUÍDO" -> {
                    btnIniciar.isEnabled = false
                    btnIniciar.text = "Concluído"
                    btnCancelar.isEnabled = false
                    btnCancelar.text = "Concluído"
                }
                "CANCELADO" -> {
                    btnIniciar.isEnabled = false
                    btnIniciar.text = "Cancelado"
                    btnCancelar.isEnabled = false
                    btnCancelar.text = "Cancelado"
                }
            }
            
            // Button click listeners
            btnIniciar.setOnClickListener { onIniciar(os) }
            btnEditar.setOnClickListener { onEditar(os) }
            btnCancelar.setOnClickListener { onCancelar(os) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_os, parent, false)
        return OSViewHolder(view)
    }

    override fun onBindViewHolder(holder: OSViewHolder, position: Int) {
        val os = ordens[position]
        holder.bind(os, onIniciarClick, onEditarClick, onCancelarClick)
        holder.itemView.setOnClickListener { onItemClick(os) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(it, os)
            true
        }
    }

    override fun getItemCount(): Int = ordens.size
}



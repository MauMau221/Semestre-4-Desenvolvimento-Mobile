package com.example.mobile.ui.theme.features.os

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.dtos.ServicoDTO
import com.google.android.material.button.MaterialButton

class ServicosAdapter(
    private val servicos: List<ServicoDTO>,
    private val onAdicionarFotoClick: (ServicoDTO) -> Unit
) : RecyclerView.Adapter<ServicosAdapter.ServicosViewHolder>() {

    class ServicosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvServicoNome: TextView = itemView.findViewById(R.id.tv_servico_nome)
        private val statusChip: TextView = itemView.findViewById(R.id.status_chip)
        private val tvPhotoCount: TextView = itemView.findViewById(R.id.tv_photo_count)
        private val btnAdicionarFoto: MaterialButton = itemView.findViewById(R.id.btn_adicionar_foto)

        fun bind(servico: ServicoDTO, onAdicionarFoto: (ServicoDTO) -> Unit) {
            tvServicoNome.text = servico.nome
            tvPhotoCount.text = "${servico.fotosCount} foto${if (servico.fotosCount != 1) "s" else ""}"
            
            // Status chip
            when (servico.status.uppercase()) {
                "CONCLUIDO" -> {
                    statusChip.text = "Concluído"
                    statusChip.setBackgroundResource(R.drawable.status_chip_concluido)
                }
                "EM_ANDAMENTO", "EM ANDAMENTO" -> {
                    statusChip.text = "Em andamento"
                    statusChip.setBackgroundResource(R.drawable.status_chip_andamento)
                }
                "PENDENTE" -> {
                    statusChip.text = "Pendente"
                    statusChip.setBackgroundResource(R.drawable.status_chip_pendente)
                }
                else -> {
                    statusChip.text = servico.status
                    statusChip.setBackgroundResource(R.drawable.status_chip_pendente)
                }
            }
            
            btnAdicionarFoto.setOnClickListener { onAdicionarFoto(servico) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servico, parent, false)
        return ServicosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicosViewHolder, position: Int) {
        val servico = servicos[position]
        holder.bind(servico, onAdicionarFotoClick)
    }

    override fun getItemCount(): Int = servicos.size
}

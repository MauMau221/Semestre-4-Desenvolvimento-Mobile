package com.example.mobile.ui.theme.features.os

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.dtos.ItemVistoriaDTO

class VistoriaAdapter(
    private val itens: List<ItemVistoriaDTO>,
    private val onEditarClick: (ItemVistoriaDTO) -> Unit,
    private val onExcluirClick: (ItemVistoriaDTO) -> Unit
) : RecyclerView.Adapter<VistoriaAdapter.VistoriaViewHolder>() {

    class VistoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitulo: TextView = itemView.findViewById(R.id.tv_item_titulo)
        private val tvDescricao: TextView = itemView.findViewById(R.id.tv_item_descricao)
        private val statusChip: TextView = itemView.findViewById(R.id.status_chip)
        private val btnEditar: ImageView = itemView.findViewById(R.id.btn_editar_item)
        private val btnExcluir: ImageView = itemView.findViewById(R.id.btn_excluir_item)

        fun bind(item: ItemVistoriaDTO, onEditar: (ItemVistoriaDTO) -> Unit, onExcluir: (ItemVistoriaDTO) -> Unit) {
            tvTitulo.text = item.titulo
            tvDescricao.text = item.descricao ?: "Sem descrição"
            
            // Status chip
            statusChip.text = item.status.uppercase()
            when (item.status.uppercase()) {
                "CRÍTICO", "CRITICO" -> {
                    statusChip.setBackgroundResource(R.drawable.status_critico)
                    statusChip.setTextColor(itemView.context.getColor(android.R.color.white))
                }
                "ATENÇÃO", "ATENCAO" -> {
                    statusChip.setBackgroundResource(R.drawable.status_atencao)
                    statusChip.setTextColor(itemView.context.getColor(android.R.color.black))
                }
                "OK" -> {
                    statusChip.setBackgroundResource(R.drawable.status_ok)
                    statusChip.setTextColor(itemView.context.getColor(android.R.color.white))
                }
                else -> {
                    statusChip.setBackgroundResource(R.drawable.status_atencao)
                    statusChip.setTextColor(itemView.context.getColor(android.R.color.black))
                }
            }
            
            btnEditar.setOnClickListener { onEditar(item) }
            btnExcluir.setOnClickListener { onExcluir(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vistoria, parent, false)
        return VistoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VistoriaViewHolder, position: Int) {
        val item = itens[position]
        holder.bind(item, onEditarClick, onExcluirClick)
    }

    override fun getItemCount(): Int = itens.size
}

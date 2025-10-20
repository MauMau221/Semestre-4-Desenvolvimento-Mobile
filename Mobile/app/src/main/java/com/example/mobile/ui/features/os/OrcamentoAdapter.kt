package com.example.mobile.ui.theme.features.os

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.dtos.ItemOrcamentoDTO
import java.text.NumberFormat
import java.util.Locale

class OrcamentoAdapter(
    private val itens: List<ItemOrcamentoDTO>,
    private val onEditarClick: (ItemOrcamentoDTO) -> Unit,
    private val onExcluirClick: (ItemOrcamentoDTO) -> Unit
) : RecyclerView.Adapter<OrcamentoAdapter.OrcamentoViewHolder>() {

    class OrcamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeChip: TextView = itemView.findViewById(R.id.type_chip)
        private val tvDescricao: TextView = itemView.findViewById(R.id.tv_descricao)
        private val tvQuantidade: TextView = itemView.findViewById(R.id.tv_quantidade)
        private val tvPrecoUnitario: TextView = itemView.findViewById(R.id.tv_preco_unitario)
        private val tvPrecoTotal: TextView = itemView.findViewById(R.id.tv_preco_total)
        private val btnEditar: ImageView = itemView.findViewById(R.id.btn_editar_item)
        private val btnExcluir: ImageView = itemView.findViewById(R.id.btn_excluir_item)

        fun bind(item: ItemOrcamentoDTO, onEditar: (ItemOrcamentoDTO) -> Unit, onExcluir: (ItemOrcamentoDTO) -> Unit) {
            tvDescricao.text = item.descricao
            tvQuantidade.text = "Qtd: ${item.quantidade}"
            
            val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            
            tvPrecoUnitario.text = "Unitário: ${formatter.format(item.precoUnitario)}"
            tvPrecoTotal.text = formatter.format(item.precoTotal)
            
            // Type chip
            when (item.tipo.uppercase()) {
                "PECA" -> {
                    typeChip.text = "Peça"
                    typeChip.setBackgroundResource(R.drawable.chip_peca)
                }
                "SERVICO" -> {
                    typeChip.text = "Serviço"
                    typeChip.setBackgroundResource(R.drawable.chip_servico)
                }
                else -> {
                    typeChip.text = "Item"
                    typeChip.setBackgroundResource(R.drawable.chip_peca)
                }
            }
            
            btnEditar.setOnClickListener { onEditar(item) }
            btnExcluir.setOnClickListener { onExcluir(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrcamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orcamento, parent, false)
        return OrcamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrcamentoViewHolder, position: Int) {
        val item = itens[position]
        holder.bind(item, onEditarClick, onExcluirClick)
    }

    override fun getItemCount(): Int = itens.size
}

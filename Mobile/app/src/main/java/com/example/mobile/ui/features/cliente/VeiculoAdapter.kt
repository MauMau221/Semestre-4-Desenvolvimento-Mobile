package com.example.mobile.ui.theme.features.cliente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.dtos.VeiculoDTO

class VeiculoAdapter(
    private val veiculos: List<VeiculoDTO>,
    private val onEditarClick: (VeiculoDTO) -> Unit,
    private val onExcluirClick: (VeiculoDTO) -> Unit
) : RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder>() {

    class VeiculoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPlaca: TextView = itemView.findViewById(R.id.tv_placa)
        private val tvModelo: TextView = itemView.findViewById(R.id.tv_modelo)
        private val tvAno: TextView = itemView.findViewById(R.id.tv_ano)
        private val tvCor: TextView = itemView.findViewById(R.id.tv_cor)
        private val tvProprietario: TextView = itemView.findViewById(R.id.tv_proprietario)
        private val btnEditar: ImageView = itemView.findViewById(R.id.btn_edit_veiculo)
        private val btnExcluir: ImageView = itemView.findViewById(R.id.btn_delete_veiculo)

        fun bind(veiculo: VeiculoDTO, onEditar: (VeiculoDTO) -> Unit, onExcluir: (VeiculoDTO) -> Unit) {
            tvPlaca.text = veiculo.placa ?: "Placa não informada"
            tvModelo.text = veiculo.modelo ?: "Modelo não informado"
            tvAno.text = "Ano: ${veiculo.ano ?: "N/A"}"
            tvCor.text = "Cor: ${veiculo.cor ?: "N/A"}"
            tvProprietario.text = "Proprietário: ${veiculo.proprietario ?: "N/A"}"
            
            btnEditar.setOnClickListener { onEditar(veiculo) }
            btnExcluir.setOnClickListener { onExcluir(veiculo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeiculoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_veiculo, parent, false)
        return VeiculoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VeiculoViewHolder, position: Int) {
        val veiculo = veiculos[position]
        holder.bind(veiculo, onEditarClick, onExcluirClick)
    }

    override fun getItemCount(): Int = veiculos.size
}

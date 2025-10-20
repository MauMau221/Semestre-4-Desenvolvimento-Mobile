package com.example.mobile.ui.theme.features.cliente

import com.example.mobile.data.remote.dtos.Cliente
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val onItemClick: (Cliente) -> Unit,
    private val onItemLongClick: (View, Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    // 1. O ViewHolder: Mapeia as views do item_cliente.xml
    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.tv_cliente_nome)
        private val cpfTextView: TextView = itemView.findViewById(R.id.tv_cliente_cpf)
        private val dataTextView: TextView = itemView.findViewById(R.id.tv_data)

        private val veiculosModeloTextView: TextView = itemView.findViewById(R.id.tv_veiculo) // Use o ID do seu layout
        private val veiculosPlacaTextView: TextView = itemView.findViewById(R.id.tv_placa)


        // Conecta os dados do cliente ás views
        fun bind(cliente: Cliente) {
            nomeTextView.text = cliente.nome ?: "Nome não informado"
            cpfTextView.text = cliente.cpfCnpj ?: "CPF/CNPJ não informado"

            // Data formatada dd/MM/yyyy
            val createdAtText = cliente.createdAt?.let {
                try {
                    val input = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                    val output = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    val date = input.parse(it)
                    output.format(date ?: java.util.Date())
                } catch (e: Exception) {
                    null
                }
            }
            dataTextView.text = createdAtText ?: "Data não disponível"

            if (cliente.veiculos.isNotEmpty()) {
                veiculosModeloTextView.text = cliente.veiculos.mapNotNull { it.modelo }.joinToString(
                    separator = ", ",
                    limit = 1,
                    truncated = " +${cliente.veiculos.size - 1}"
                )
                veiculosPlacaTextView.text = cliente.veiculos.mapNotNull { it.placa }.joinToString(
                    separator = ", ",
                    limit = 1,
                    truncated = " +${cliente.veiculos.size - 1}"
                )
            } else {
                veiculosModeloTextView.text = "Sem veículos"
                veiculosPlacaTextView.text = "—"
            }
        }
    }

    //Chamado quando o RecyclerView precisa de um novo item
    //Ele infla o XML do item e cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)

        return ClienteViewHolder(view)
    }

    //Chamado para exibir os dados em uma posição especifica
    //Ele pega um ViewHolder existente e usa a função bind para atualiza-lo
    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.bind(cliente)
        holder.itemView.setOnClickListener { onItemClick(cliente) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(it, cliente)
            true
        }
    }

    //Informa ao RecyclerView quantos itens tem na lista
    override fun getItemCount(): Int {
        return clientes.size
    }
}

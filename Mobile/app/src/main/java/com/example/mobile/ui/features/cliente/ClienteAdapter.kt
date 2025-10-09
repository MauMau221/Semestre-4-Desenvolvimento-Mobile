package com.example.mobile.ui.theme.features.cliente

import Cliente
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
class ClienteAdapter(private val clientes: List<Cliente>) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    // 1. O ViewHolder: Mapeia as views do item_cliente.xml
    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.tv_cliente_nome)
        private val cpfTextView: TextView = itemView.findViewById(R.id.tv_cliente_cpf)
        private val dataTextView: TextView = itemView.findViewById(R.id.tv_data)

        private val veiculosModeloTextView: TextView = itemView.findViewById(R.id.tv_veiculo) // Use o ID do seu layout
        private val veiculosPlacaTextView: TextView = itemView.findViewById(R.id.tv_placa)


        // Conecta os dados do cliente ás views
        fun bind(cliente: Cliente) {
            nomeTextView.text = cliente.nome
            cpfTextView.text = cliente.cpfCnpj
            dataTextView.text = cliente.createdAt


            if (cliente.veiculos.isNotEmpty()) {
            veiculosModeloTextView.text = cliente.veiculos.joinToString { it.modelo.toString() }
            veiculosPlacaTextView.text = cliente.veiculos.joinToString { it.placa.toString() }
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
    }

    //Informa ao RecyclerView quantos itens tem na lista
    override fun getItemCount(): Int {
        return clientes.size
    }
}

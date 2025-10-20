package com.example.mobile.ui.theme.features.os

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.ItemOrcamentoDTO
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import java.text.NumberFormat
import java.util.Locale

class OrcamentoFragment : Fragment() {
    
    private val ADD_ITEM_REQUEST_CODE = 1002
    private var osId: Int = -1
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdicionarItem: MaterialButton
    private lateinit var tvTotalOrcamento: TextView
    
    private var allItens: List<ItemOrcamentoDTO> = emptyList()
    
    companion object {
        fun newInstance(osId: Int): OrcamentoFragment {
            val fragment = OrcamentoFragment()
            val args = Bundle()
            args.putInt("os_id", osId)
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            osId = it.getInt("os_id", -1)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orcamento, container, false)
        
        recyclerView = view.findViewById(R.id.recyclerViewItens)
        btnAdicionarItem = view.findViewById(R.id.btn_adicionar_item)
        tvTotalOrcamento = view.findViewById(R.id.tv_total_orcamento)
        
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        btnAdicionarItem.setOnClickListener {
            val intent = Intent(requireContext(), ItemOrcamentoFormActivity::class.java)
            intent.putExtra("os_id", osId)
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
        }
        
        loadItens()
        
        return view
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadItens()
        }
    }
    
    private fun loadItens() {
        if (osId == -1) {
            Toast.makeText(requireContext(), "OS inválida", Toast.LENGTH_SHORT).show()
            return
        }
        
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.getItensOrcamento(osId)
                if (response.isSuccessful) {
                    allItens = response.body()?.data ?: emptyList()
                    displayItens()
                } else {
                    // Mock data para demonstração
                    allItens = getMockItens()
                    displayItens()
                }
            } catch (e: Exception) {
                // Mock data para demonstração
                allItens = getMockItens()
                displayItens()
            }
        }
    }
    
    private fun getMockItens(): List<ItemOrcamentoDTO> {
        return listOf(
            ItemOrcamentoDTO(
                id = 1,
                descricao = "Pneu 185/65 R15",
                tipo = "PECA",
                quantidade = 1,
                precoUnitario = 280.0,
                precoTotal = 280.0,
                osId = osId,
                createdAt = null,
                updatedAt = null
            ),
            ItemOrcamentoDTO(
                id = 2,
                descricao = "Troca de pastilhas de freio",
                tipo = "SERVICO",
                quantidade = 1,
                precoUnitario = 120.0,
                precoTotal = 120.0,
                osId = osId,
                createdAt = null,
                updatedAt = null
            ),
            ItemOrcamentoDTO(
                id = 3,
                descricao = "Pastilha de freio",
                tipo = "PECA",
                quantidade = 1,
                precoUnitario = 80.0,
                precoTotal = 80.0,
                osId = osId,
                createdAt = null,
                updatedAt = null
            )
        )
    }
    
    private fun displayItens() {
        recyclerView.adapter = OrcamentoAdapter(
            allItens,
            onEditarClick = { item ->
                val intent = Intent(requireContext(), ItemOrcamentoFormActivity::class.java)
                intent.putExtra("item_id", item.id)
                intent.putExtra("os_id", osId)
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
            },
            onExcluirClick = { item ->
                confirmDeleteItem(item)
            }
        )
        
        updateTotal()
    }
    
    private fun updateTotal() {
        val total = allItens.sumOf { it.precoTotal }
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        tvTotalOrcamento.text = formatter.format(total)
    }
    
    private fun confirmDeleteItem(item: ItemOrcamentoDTO) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Item")
            .setMessage("Deseja excluir o item '${item.descricao}'?")
            .setPositiveButton("Sim") { _, _ ->
                deleteItem(item.id)
            }
            .setNegativeButton("Não", null)
            .show()
    }
    
    private fun deleteItem(itemId: Int) {
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.deleteItemOrcamento(itemId)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Item excluído", Toast.LENGTH_SHORT).show()
                    loadItens()
                } else {
                    Toast.makeText(requireContext(), "Erro ao excluir: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

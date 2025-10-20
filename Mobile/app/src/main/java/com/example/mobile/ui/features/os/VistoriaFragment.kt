package com.example.mobile.ui.theme.features.os

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.example.mobile.data.remote.dtos.ItemVistoriaDTO
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog

class VistoriaFragment : Fragment() {
    
    private val ADD_ITEM_REQUEST_CODE = 1001
    private var osId: Int = -1
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnNovoItem: MaterialButton
    
    private var allItens: List<ItemVistoriaDTO> = emptyList()
    
    companion object {
        fun newInstance(osId: Int): VistoriaFragment {
            val fragment = VistoriaFragment()
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
        val view = inflater.inflate(R.layout.fragment_vistoria, container, false)
        
        recyclerView = view.findViewById(R.id.recyclerViewItens)
        btnNovoItem = view.findViewById(R.id.btn_novo_item)
        
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        btnNovoItem.setOnClickListener {
            val intent = Intent(requireContext(), ItemVistoriaFormActivity::class.java)
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
                val response = api.getItensVistoria(osId)
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
    
    private fun getMockItens(): List<ItemVistoriaDTO> {
        return listOf(
            ItemVistoriaDTO(
                id = 1,
                titulo = "Pneu dianteiro direito",
                descricao = "Desgaste irregular",
                status = "Crítico",
                osId = osId,
                createdAt = null,
                updatedAt = null
            ),
            ItemVistoriaDTO(
                id = 2,
                titulo = "Freios dianteiros",
                descricao = "Pastilhas no limite",
                status = "Atenção",
                osId = osId,
                createdAt = null,
                updatedAt = null
            ),
            ItemVistoriaDTO(
                id = 3,
                titulo = "Óleo do motor",
                descricao = "Nível adequado",
                status = "OK",
                osId = osId,
                createdAt = null,
                updatedAt = null
            )
        )
    }
    
    private fun displayItens() {
        recyclerView.adapter = VistoriaAdapter(
            allItens,
            onEditarClick = { item ->
                val intent = Intent(requireContext(), ItemVistoriaFormActivity::class.java)
                intent.putExtra("item_id", item.id)
                intent.putExtra("os_id", osId)
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
            },
            onExcluirClick = { item ->
                confirmDeleteItem(item)
            }
        )
    }
    
    private fun confirmDeleteItem(item: ItemVistoriaDTO) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Item")
            .setMessage("Deseja excluir o item '${item.titulo}'?")
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
                val response = api.deleteItemVistoria(itemId)
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

package com.example.mobile.ui.theme.features.os

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
import com.example.mobile.data.remote.dtos.ServicoDTO
import kotlinx.coroutines.launch

class ServicosFragment : Fragment() {
    
    private var osId: Int = -1
    
    private lateinit var recyclerView: RecyclerView
    
    companion object {
        fun newInstance(osId: Int): ServicosFragment {
            val fragment = ServicosFragment()
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
        val view = inflater.inflate(R.layout.fragment_servicos, container, false)
        
        recyclerView = view.findViewById(R.id.recyclerViewServicos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        loadServicos()
        
        return view
    }
    
    private fun loadServicos() {
        if (osId == -1) {
            Toast.makeText(requireContext(), "OS inválida", Toast.LENGTH_SHORT).show()
            return
        }
        
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.getServicos(osId)
                if (response.isSuccessful) {
                    val servicos = response.body()?.data ?: emptyList()
                    displayServicos(servicos)
                } else {
                    // Mock data para demonstração
                    val mockServicos = getMockServicos()
                    displayServicos(mockServicos)
                }
            } catch (e: Exception) {
                // Mock data para demonstração
                val mockServicos = getMockServicos()
                displayServicos(mockServicos)
            }
        }
    }
    
    private fun getMockServicos(): List<ServicoDTO> {
        return listOf(
            ServicoDTO(
                id = 1,
                nome = "Troca de pneu",
                status = "CONCLUIDO",
                osId = osId,
                descricao = "Troca do pneu dianteiro direito",
                fotosCount = 1,
                createdAt = null,
                updatedAt = null
            ),
            ServicoDTO(
                id = 2,
                nome = "Troca de pastilhas",
                status = "EM_ANDAMENTO",
                osId = osId,
                descricao = "Troca das pastilhas de freio dianteiras",
                fotosCount = 0,
                createdAt = null,
                updatedAt = null
            )
        )
    }
    
    private fun displayServicos(servicos: List<ServicoDTO>) {
        recyclerView.adapter = ServicosAdapter(
            servicos,
            onAdicionarFotoClick = { servico ->
                // TODO: Implementar adição de fotos
                Toast.makeText(requireContext(), "Adicionar foto para: ${servico.nome}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

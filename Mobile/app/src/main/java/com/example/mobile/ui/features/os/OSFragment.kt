package com.example.mobile.ui.theme.features.os

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.remote.RetrofitClient
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import com.example.mobile.data.remote.dtos.OrdemServicoDTO
import android.content.res.ColorStateList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OSFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OSFragment : Fragment() {
    private val ADD_OS_REQUEST_CODE = 1
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvPendentesCount: TextView
    private lateinit var tvAndamentoCount: TextView
    private lateinit var tvConcluidasCount: TextView
    private lateinit var tvCanceladasCount: TextView
    private lateinit var chipTodas: Chip
    private lateinit var chipPendentes: Chip
    private lateinit var chipAndamento: Chip
    private lateinit var chipConcluidas: Chip
    
    private var allOS: List<OrdemServicoDTO> = emptyList()
    private var currentFilter = "TODAS"
    
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_o_s, container, false)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewOS)
        tvPendentesCount = view.findViewById(R.id.tv_pendentes_count)
        tvAndamentoCount = view.findViewById(R.id.tv_andamento_count)
        tvConcluidasCount = view.findViewById(R.id.tv_concluidas_count)
        tvCanceladasCount = view.findViewById(R.id.tv_canceladas_count)
        chipTodas = view.findViewById(R.id.chip_todas)
        chipPendentes = view.findViewById(R.id.chip_pendentes)
        chipAndamento = view.findViewById(R.id.chip_andamento)
        chipConcluidas = view.findViewById(R.id.chip_concluidas)
        
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_os)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Setup filter chips
        setupFilterChips()
        
        // Setup FAB
        fab.setOnClickListener {
            val intent = Intent(requireContext(), AddOSActivity::class.java)
            startActivityForResult(intent, ADD_OS_REQUEST_CODE)
        }

        loadOS()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_OS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadOS()
        }
    }

    private fun setupFilterChips() {
        chipTodas.setOnClickListener { setFilter("TODAS") }
        chipPendentes.setOnClickListener { setFilter("PENDENTE") }
        chipAndamento.setOnClickListener { setFilter("EM ANDAMENTO") }
        chipConcluidas.setOnClickListener { setFilter("CONCLUIDO") }
    }
    
    private fun setFilter(filter: String) {
        currentFilter = filter
        
        // Update chip selection
        chipTodas.isSelected = filter == "TODAS"
        chipPendentes.isSelected = filter == "PENDENTE"
        chipAndamento.isSelected = filter == "EM ANDAMENTO"
        chipConcluidas.isSelected = filter == "CONCLUIDO"
        
        // Update chip colors
        updateChipColors()
        
        // Filter and display OS
        filterAndDisplayOS()
    }
    
    private fun updateChipColors() {
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.chip_selected)
        val unselectedColor = ContextCompat.getColor(requireContext(), R.color.chip_unselected)

        chipTodas.setChipBackgroundColor(ColorStateList.valueOf(if (chipTodas.isSelected) selectedColor else unselectedColor))
        chipPendentes.setChipBackgroundColor(ColorStateList.valueOf(if (chipPendentes.isSelected) selectedColor else unselectedColor))
        chipAndamento.setChipBackgroundColor(ColorStateList.valueOf(if (chipAndamento.isSelected) selectedColor else unselectedColor))
        chipConcluidas.setChipBackgroundColor(ColorStateList.valueOf(if (chipConcluidas.isSelected) selectedColor else unselectedColor))
    }
    
    private fun filterAndDisplayOS() {
        val filteredList = if (currentFilter == "TODAS") {
            allOS
        } else {
            allOS.filter { it.status?.uppercase() == currentFilter }
        }
        
        recyclerView.adapter = OSAdapter(
            filteredList,
                    onItemClick = { os ->
                        val intent = Intent(requireContext(), OSDetalhesActivity::class.java)
                        intent.putExtra("os_id", os.id)
                        startActivity(intent)
                    },
            onItemLongClick = { _, os ->
                showOptionsDialog(os)
            },
            onIniciarClick = { os ->
                iniciarOS(os)
            },
            onEditarClick = { os ->
                val intent = Intent(requireContext(), AddOSActivity::class.java)
                intent.putExtra("os_id", os.id)
                startActivityForResult(intent, ADD_OS_REQUEST_CODE)
            },
            onCancelarClick = { os ->
                cancelarOS(os)
            }
        )
    }
    
    private fun loadOS() {
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.getOrdensServico()
                if (response.isSuccessful) {
                    allOS = response.body()?.data ?: emptyList()
                    updateCounters()
                    filterAndDisplayOS()
                } else {
                    Toast.makeText(requireContext(), "Erro ao carregar OS: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateCounters() {
        val pendentes = allOS.count { it.status?.uppercase() == "PENDENTE" }
        val andamento = allOS.count { it.status?.uppercase() == "EM ANDAMENTO" || it.status?.uppercase() == "ANDAMENTO" }
        val concluidas = allOS.count { it.status?.uppercase() == "CONCLUIDO" || it.status?.uppercase() == "CONCLUÍDO" }
        val canceladas = allOS.count { it.status?.uppercase() == "CANCELADO" }
        
        tvPendentesCount.text = pendentes.toString()
        tvAndamentoCount.text = andamento.toString()
        tvConcluidasCount.text = concluidas.toString()
        tvCanceladasCount.text = canceladas.toString()
    }

    private fun showOptionsDialog(os: OrdemServicoDTO) {
        val options = arrayOf("Editar", "Excluir")
        AlertDialog.Builder(requireContext())
            .setTitle("OS #${os.id}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(requireContext(), AddOSActivity::class.java)
                        intent.putExtra("os_id", os.id)
                        startActivityForResult(intent, ADD_OS_REQUEST_CODE)
                    }
                    1 -> confirmDelete(os)
                }
            }
            .show()
    }

    private fun confirmDelete(os: OrdemServicoDTO) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir OS #${os.id}")
            .setMessage("Confirma a exclusão?")
            .setPositiveButton("Excluir") { _, _ -> deleteOS(os.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteOS(id: Int) {
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val response = api.deleteOrdemServico(id)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "OS excluída", Toast.LENGTH_SHORT).show()
                    loadOS()
                } else {
                    Toast.makeText(requireContext(), "Erro ao excluir: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun iniciarOS(os: OrdemServicoDTO) {
        AlertDialog.Builder(requireContext())
            .setTitle("Iniciar OS")
            .setMessage("Deseja iniciar a OS #${os.id}?")
            .setPositiveButton("Sim") { _, _ ->
                updateOSStatus(os.id, "EM ANDAMENTO")
            }
            .setNegativeButton("Não", null)
            .show()
    }
    
    private fun cancelarOS(os: OrdemServicoDTO) {
        AlertDialog.Builder(requireContext())
            .setTitle("Cancelar OS")
            .setMessage("Deseja cancelar a OS #${os.id}?")
            .setPositiveButton("Sim") { _, _ ->
                updateOSStatus(os.id, "CANCELADO")
            }
            .setNegativeButton("Não", null)
            .show()
    }
    
    private fun updateOSStatus(id: Int, status: String) {
        val api = RetrofitClient.getInstance(requireContext())
        lifecycleScope.launch {
            try {
                val request = com.example.mobile.data.remote.dtos.NewOrdemServicoRequest(
                    cliente_id = 1, // Mock - should get from current OS
                    descricao = null,
                    status = status,
                    valor_total = null
                )
                val response = api.updateOrdemServico(id, request)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Status atualizado para $status", Toast.LENGTH_SHORT).show()
                    loadOS()
                } else {
                    Toast.makeText(requireContext(), "Erro ao atualizar status: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Falha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OSFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OSFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.example.mobile.ui.theme.features.os

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobile.R
import com.google.android.material.button.MaterialButton

class EntregaFragment : Fragment() {
    
    private var osId: Int = -1
    
    private lateinit var btnConfirmarEntrega: MaterialButton
    
    // Vistoria checkboxes
    private lateinit var checkPneuDianteiro: CheckBox
    private lateinit var checkFreios: CheckBox
    private lateinit var checkOleo: CheckBox
    
    // Orçamento checkboxes
    private lateinit var checkOrcPneu: CheckBox
    private lateinit var checkOrcFreios: CheckBox
    private lateinit var checkOrcOleo: CheckBox
    
    companion object {
        fun newInstance(osId: Int): EntregaFragment {
            val fragment = EntregaFragment()
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
        val view = inflater.inflate(R.layout.fragment_entrega, container, false)
        
        initViews(view)
        setupListeners()
        
        return view
    }
    
    private fun initViews(view: View) {
        btnConfirmarEntrega = view.findViewById(R.id.btn_confirmar_entrega)
        
        // Vistoria checkboxes
        checkPneuDianteiro = view.findViewById(R.id.check_pneu_dianteiro)
        checkFreios = view.findViewById(R.id.check_freios)
        checkOleo = view.findViewById(R.id.check_oleo)
        
        // Orçamento checkboxes
        checkOrcPneu = view.findViewById(R.id.check_orc_pneu)
        checkOrcFreios = view.findViewById(R.id.check_orc_freios)
        checkOrcOleo = view.findViewById(R.id.check_orc_oleo)
    }
    
    private fun setupListeners() {
        btnConfirmarEntrega.setOnClickListener {
            confirmarEntrega()
        }
    }
    
    private fun confirmarEntrega() {
        val vistoriaCompleta = checkPneuDianteiro.isChecked && 
                              checkFreios.isChecked && 
                              checkOleo.isChecked
        
        val orcamentoCompleto = checkOrcPneu.isChecked && 
                               checkOrcFreios.isChecked && 
                               checkOrcOleo.isChecked
        
        if (!vistoriaCompleta) {
            Toast.makeText(requireContext(), "Todos os itens da vistoria devem ser verificados", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (!orcamentoCompleto) {
            Toast.makeText(requireContext(), "Todos os itens do orçamento devem ser verificados", Toast.LENGTH_SHORT).show()
            return
        }
        
        // TODO: Implementar confirmação de entrega via API
        Toast.makeText(requireContext(), "Entrega confirmada com sucesso!", Toast.LENGTH_SHORT).show()
    }
}

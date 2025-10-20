package com.example.mobile.ui.theme.features.os

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobile.R
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale

class PagamentoFragment : Fragment() {
    
    private var osId: Int = -1
    
    private lateinit var tvSubtotal: TextView
    private lateinit var tvDesconto: TextView
    private lateinit var tvTotal: TextView
    private lateinit var radioDinheiro: RadioButton
    private lateinit var radioCartao: RadioButton
    private lateinit var radioPix: RadioButton
    private lateinit var btnConfirmarPagamento: MaterialButton
    
    companion object {
        fun newInstance(osId: Int): PagamentoFragment {
            val fragment = PagamentoFragment()
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
        val view = inflater.inflate(R.layout.fragment_pagamento, container, false)
        
        initViews(view)
        setupListeners()
        loadFinancialData()
        
        return view
    }
    
    private fun initViews(view: View) {
        tvSubtotal = view.findViewById(R.id.tv_subtotal)
        tvDesconto = view.findViewById(R.id.tv_desconto)
        tvTotal = view.findViewById(R.id.tv_total)
        radioDinheiro = view.findViewById(R.id.radio_dinheiro)
        radioCartao = view.findViewById(R.id.radio_cartao)
        radioPix = view.findViewById(R.id.radio_pix)
        btnConfirmarPagamento = view.findViewById(R.id.btn_confirmar_pagamento)
    }
    
    private fun setupListeners() {
        btnConfirmarPagamento.setOnClickListener {
            confirmarPagamento()
        }
    }
    
    private fun loadFinancialData() {
        // Mock data - em produção viria da API
        val subtotal = 480.0
        val desconto = 0.0
        val total = subtotal - desconto
        
        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        
        tvSubtotal.text = formatter.format(subtotal)
        tvDesconto.text = formatter.format(desconto)
        tvTotal.text = formatter.format(total)
    }
    
    private fun confirmarPagamento() {
        val formaPagamento = when {
            radioDinheiro.isChecked -> "Dinheiro"
            radioCartao.isChecked -> "Cartão"
            radioPix.isChecked -> "PIX"
            else -> null
        }
        
        if (formaPagamento == null) {
            Toast.makeText(requireContext(), "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show()
            return
        }
        
        // TODO: Implementar confirmação de pagamento via API
        Toast.makeText(requireContext(), "Pagamento confirmado via $formaPagamento!", Toast.LENGTH_SHORT).show()
    }
}

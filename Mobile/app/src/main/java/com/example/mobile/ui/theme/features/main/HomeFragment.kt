package com.example.mobile.ui.theme.features.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.RetrofitClient
import com.example.mobile.ui.theme.features.clientes.ClienteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.firstOrNull

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Referencia para o recyclerView no layout
        val recyclierView = view.findViewById<RecyclerView>(R.id.recyclerViewClientes)

        //Define como os itens serão organizados (Lista vertical)
        recyclierView.layoutManager = LinearLayoutManager(requireContext())

        //Pega a instancia do serviço de API
        val api = RetrofitClient.getInstance(requireContext())

        lifecycleScope.launch {
            try {
                val response = api.getClientes()
                if (response.isSuccessful) {
                    val clientes = response.body()

                    Log.d("HOME_ACTIVITY_DEBUG", "O corpo da resposta é nulo? ${clientes == null}")


                    // Acessa a lista de clientes DENTRO do objeto de resposta
                    val listaDeClientes = clientes?.data
                    Log.d("HOME_ACTIVITY_DEBUG", "A lista 'data' é nula? ${listaDeClientes == null}")
                    Log.d("HOME_ACTIVITY_DEBUG", "Tamanho da lista: ${listaDeClientes?.size}")

                    if (listaDeClientes?.isNotEmpty() == true) {
                        Log.d("HOME_ACTIVITY_DEBUG", "Primeiro cliente: ${listaDeClientes.firstOrNull()}")
                    }


                    if (!listaDeClientes.isNullOrEmpty()) {
                        //cria o adapter com lista e conecta no recyclerView
                        val adapter = ClienteAdapter(listaDeClientes)
                        recyclierView.adapter = adapter
                    }else {
                        Toast.makeText(requireContext(), "Nenhum cliente encontrado", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(requireContext(), "Erro ao carregar clientes: ${response.code()}",
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("HOME_ACTIVITY", "Falha na conexão/parsing: ${e.message}")
                    Toast.makeText(
                        requireContext(),
                        "Falha na conexão com o servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        return view;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
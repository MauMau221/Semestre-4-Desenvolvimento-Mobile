package com.example.mobile.data.models.clientes

import com.example.mobile.data.models.veiculos.Veiculos
import com.google.gson.annotations.SerializedName

data class Clientes(
    val id: Int,
    val nome: String,
    val cpf: String,
    val telefone: String,
    val email: String,

    @SerializedName("veiculos")
    val veiculos: List<Veiculos>
)
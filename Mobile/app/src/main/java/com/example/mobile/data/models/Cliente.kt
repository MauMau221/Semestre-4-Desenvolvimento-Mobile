package com.example.mobile.data.models

import com.example.mobile.data.models.Veiculo
import com.google.gson.annotations.SerializedName

data class Cliente(
    val id: Int,
    val nome: String,
    val cpf: String,
    val telefone: String,
    val email: String,

    @SerializedName("veiculos")
    val veiculos: List<Veiculo>
)
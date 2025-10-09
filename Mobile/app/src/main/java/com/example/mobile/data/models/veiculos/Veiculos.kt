package com.example.mobile.data.models.veiculos

import com.google.gson.annotations.SerializedName

data class Veiculos(
    @SerializedName("id")
    val id: Int,

    @SerializedName("modelo")
    val modelo: String,

    @SerializedName("placa")
    val placa: String,

    @SerializedName("ano")
    val ano: Int,

    @SerializedName("marca")
    val marca: String,

    @SerializedName("cor")
    val cor: String
)

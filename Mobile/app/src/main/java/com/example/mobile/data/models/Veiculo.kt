package com.example.mobile.data.models

import com.google.gson.annotations.SerializedName

data class Veiculo(
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
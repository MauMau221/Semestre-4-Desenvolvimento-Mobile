package com.example.mobile.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class VeiculoDTO(
    val id: Int,
    val modelo: String?,
    val placa: String?,
    val ano: Int?,
    val marca: String?,
    val cor: String?,
    @SerializedName("cliente_id") val clienteId: Int,
    @SerializedName("proprietario") val proprietario: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class NewVeiculoRequest(
    val modelo: String,
    val placa: String,
    val ano: Int?,
    val marca: String?,
    val cor: String?,
    @SerializedName("cliente_id") val clienteId: Int,
    val proprietario: String?
)

data class VeiculoResponse(
    val data: List<VeiculoDTO>,
    val total: Int,
    @SerializedName("current_page") val currentPage: Int
)

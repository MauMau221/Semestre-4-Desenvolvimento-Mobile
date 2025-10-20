package com.example.mobile.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class ItemOrcamentoDTO(
    val id: Int,
    val descricao: String,
    val tipo: String, // "PECA" ou "SERVICO"
    val quantidade: Int,
    @SerializedName("preco_unitario") val precoUnitario: Double,
    @SerializedName("preco_total") val precoTotal: Double,
    @SerializedName("os_id") val osId: Int,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class NewItemOrcamentoRequest(
    val descricao: String,
    val tipo: String,
    val quantidade: Int,
    @SerializedName("preco_unitario") val precoUnitario: Double,
    @SerializedName("os_id") val osId: Int
)

data class ItemOrcamentoResponse(
    val data: List<ItemOrcamentoDTO>,
    val total: Int,
    @SerializedName("current_page") val currentPage: Int
)

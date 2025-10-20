package com.example.mobile.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class OrdemServicoResponse(
    val data: List<OrdemServicoDTO>,
    val total: Int,
    @SerializedName("current_page") val currentPage: Int
)

data class OrdemServicoDTO(
    val id: Int,
    @SerializedName("cliente_id") val clienteId: Int,
    val status: String?,
    val descricao: String?,
    @SerializedName("valor_total") val valorTotal: Double?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)



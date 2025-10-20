package com.example.mobile.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class ServicoDTO(
    val id: Int,
    val nome: String,
    val status: String, // "PENDENTE", "EM_ANDAMENTO", "CONCLUIDO"
    @SerializedName("os_id") val osId: Int,
    val descricao: String?,
    @SerializedName("fotos_count") val fotosCount: Int = 0,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class NewServicoRequest(
    val nome: String,
    val status: String,
    @SerializedName("os_id") val osId: Int,
    val descricao: String?
)

data class ServicoResponse(
    val data: List<ServicoDTO>,
    val total: Int,
    @SerializedName("current_page") val currentPage: Int
)

package com.example.mobile.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class ItemVistoriaDTO(
    val id: Int,
    val titulo: String,
    val descricao: String?,
    val status: String,
    @SerializedName("os_id") val osId: Int,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class NewItemVistoriaRequest(
    val titulo: String,
    val descricao: String?,
    val status: String,
    @SerializedName("os_id") val osId: Int
)

data class ItemVistoriaResponse(
    val data: List<ItemVistoriaDTO>,
    val total: Int,
    @SerializedName("current_page") val currentPage: Int
)

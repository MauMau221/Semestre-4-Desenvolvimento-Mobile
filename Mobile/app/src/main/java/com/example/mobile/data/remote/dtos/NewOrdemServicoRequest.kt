package com.example.mobile.data.remote.dtos

data class NewOrdemServicoRequest(
    val cliente_id: Int,
    val descricao: String?,
    val status: String?,
    val valor_total: Double?
)



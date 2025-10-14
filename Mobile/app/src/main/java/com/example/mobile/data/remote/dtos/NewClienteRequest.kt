package com.example.mobile.data.remote.dtos

data class NewClienteRequest(
    val nome: String,
    val cpf_cnpj: String,
    val telefone: String?,
    val email: String?,
    val endereco: String?
)

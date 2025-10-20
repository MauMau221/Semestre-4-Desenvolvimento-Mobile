package com.example.mobile.data.remote

import com.example.mobile.data.remote.dtos.ClienteResponse
import com.example.mobile.data.remote.dtos.Cliente as ClienteDTO
import com.example.mobile.data.models.User
import com.example.mobile.data.remote.dtos.LoginRequest
import com.example.mobile.data.remote.dtos.LoginResponse
import com.example.mobile.data.remote.dtos.NewClienteRequest
import com.example.mobile.data.remote.dtos.OrdemServicoResponse
import com.example.mobile.data.remote.dtos.NewOrdemServicoRequest
import com.example.mobile.data.remote.dtos.VeiculoDTO
import com.example.mobile.data.remote.dtos.NewVeiculoRequest
import com.example.mobile.data.remote.dtos.VeiculoResponse
import com.example.mobile.data.remote.dtos.ItemVistoriaDTO
import com.example.mobile.data.remote.dtos.NewItemVistoriaRequest
import com.example.mobile.data.remote.dtos.ItemVistoriaResponse
import com.example.mobile.data.remote.dtos.ItemOrcamentoDTO
import com.example.mobile.data.remote.dtos.NewItemOrcamentoRequest
import com.example.mobile.data.remote.dtos.ItemOrcamentoResponse
import com.example.mobile.data.remote.dtos.ServicoDTO
import com.example.mobile.data.remote.dtos.NewServicoRequest
import com.example.mobile.data.remote.dtos.ServicoResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


    @POST("me")
    suspend fun checkToken(@Header("Authorization") token: String): Response<User>

    @GET("clientes")
    suspend fun getClientes(): Response<ClienteResponse>

    @POST("clientes")
    suspend fun createCliente(@Body newClient: NewClienteRequest): Response<Void>

    @GET("clientes/{id}")
    suspend fun getCliente(@Path("id") id: Int): Response<ClienteDTO>

    @PUT("clientes/{id}")
    suspend fun updateCliente(
        @Path("id") id: Int,
        @Body request: NewClienteRequest
    ): Response<Void>

    @DELETE("clientes/{id}")
    suspend fun deleteCliente(@Path("id") id: Int): Response<Void>

    // Ordem de Serviço
    @GET("ordens-servico")
    suspend fun getOrdensServico(): Response<OrdemServicoResponse>

    @GET("ordens-servico/{id}")
    suspend fun getOrdemServico(@Path("id") id: Int): Response<com.example.mobile.data.remote.dtos.OrdemServicoDTO>

    @POST("ordens-servico")
    suspend fun createOrdemServico(@Body request: NewOrdemServicoRequest): Response<Void>

    @PUT("ordens-servico/{id}")
    suspend fun updateOrdemServico(
        @Path("id") id: Int,
        @Body request: NewOrdemServicoRequest
    ): Response<Void>

    @DELETE("ordens-servico/{id}")
    suspend fun deleteOrdemServico(@Path("id") id: Int): Response<Void>

    // Veículos
    @GET("veiculos")
    suspend fun getVeiculos(): Response<VeiculoResponse>

    @GET("veiculos/{id}")
    suspend fun getVeiculo(@Path("id") id: Int): Response<VeiculoDTO>

    @GET("veiculos/cliente/{cliente_id}")
    suspend fun getVeiculosByCliente(@Path("cliente_id") clienteId: Int): Response<VeiculoResponse>

    @POST("veiculos")
    suspend fun createVeiculo(@Body request: NewVeiculoRequest): Response<Void>

    @PUT("veiculos/{id}")
    suspend fun updateVeiculo(
        @Path("id") id: Int,
        @Body request: NewVeiculoRequest
    ): Response<Void>

    @DELETE("veiculos/{id}")
    suspend fun deleteVeiculo(@Path("id") id: Int): Response<Void>

    // Itens de Vistoria
    @GET("vistoria/{os_id}")
    suspend fun getItensVistoria(@Path("os_id") osId: Int): Response<ItemVistoriaResponse>

    @GET("vistoria/item/{id}")
    suspend fun getItemVistoria(@Path("id") id: Int): Response<ItemVistoriaDTO>

    @POST("vistoria")
    suspend fun createItemVistoria(@Body request: NewItemVistoriaRequest): Response<Void>

    @PUT("vistoria/{id}")
    suspend fun updateItemVistoria(
        @Path("id") id: Int,
        @Body request: NewItemVistoriaRequest
    ): Response<Void>

    @DELETE("vistoria/{id}")
    suspend fun deleteItemVistoria(@Path("id") id: Int): Response<Void>

    // Itens de Orçamento
    @GET("orcamento/{os_id}")
    suspend fun getItensOrcamento(@Path("os_id") osId: Int): Response<ItemOrcamentoResponse>

    @GET("orcamento/item/{id}")
    suspend fun getItemOrcamento(@Path("id") id: Int): Response<ItemOrcamentoDTO>

    @POST("orcamento")
    suspend fun createItemOrcamento(@Body request: NewItemOrcamentoRequest): Response<Void>

    @PUT("orcamento/{id}")
    suspend fun updateItemOrcamento(
        @Path("id") id: Int,
        @Body request: NewItemOrcamentoRequest
    ): Response<Void>

    @DELETE("orcamento/{id}")
    suspend fun deleteItemOrcamento(@Path("id") id: Int): Response<Void>

    // Serviços
    @GET("servicos/{os_id}")
    suspend fun getServicos(@Path("os_id") osId: Int): Response<ServicoResponse>

    @GET("servicos/item/{id}")
    suspend fun getServico(@Path("id") id: Int): Response<ServicoDTO>

    @POST("servicos")
    suspend fun createServico(@Body request: NewServicoRequest): Response<Void>

    @PUT("servicos/{id}")
    suspend fun updateServico(
        @Path("id") id: Int,
        @Body request: NewServicoRequest
    ): Response<Void>

    @DELETE("servicos/{id}")
    suspend fun deleteServico(@Path("id") id: Int): Response<Void>

}
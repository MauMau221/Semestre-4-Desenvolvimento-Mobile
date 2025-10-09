import com.google.gson.annotations.SerializedName

// Classe que representa o objeto de paginação COMPLETO
data class ClienteResponse(
    val data: List<Cliente>,
    val total: Int,
    @SerializedName("current_page") val currentPage: Int
)

data class Veiculo(
    val id: Int,
    val modelo: String?,
    val placa: String?,
    val ano: Int?,
    val marca: String?,
    val cor: String?
)

data class Cliente(
    val id: Int,
    val nome: String?,
    val email: String?,
    @SerializedName("cpf_cnpj") val cpfCnpj: String?,
    val telefone: String?,
    val endereco: String?,

    @SerializedName("veiculos")
    val veiculos: List<Veiculo>,

    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
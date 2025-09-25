<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Cliente extends Model
{
    use HasFactory;

    protected $fillable = [
        'nome',
        'cpf_cnpj',
        'telefone',
        'email',
        'endereco',
    ];

    /**
     * Get the vehicles for the client.
     */
    public function veiculos()
    {
        return $this->hasMany(Veiculo::class);
    }

    /**
     * Get the service orders for the client.
     */
    public function ordensServico()
    {
        return $this->hasMany(OrdemServico::class);
    }

    /**
     * Get the appointments for the client.
     */
    public function agendamentos()
    {
        return $this->hasMany(Agendamento::class);
    }

    /**
     * Get the quotes for the client.
     */
    public function orcamentos()
    {
        return $this->hasMany(Orcamento::class);
    }
}

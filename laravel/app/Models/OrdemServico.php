<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class OrdemServico extends Model
{
    use HasFactory;

    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'ordem_servicos';

    protected $fillable = [
        'cliente_id',
        'veiculo_id',
        'responsavel_id',
        'data_abertura',
        'data_conclusao',
        'descricao',
        'status',
    ];

    /**
     * Get the client for the service order.
     */
    public function cliente()
    {
        return $this->belongsTo(Cliente::class);
    }

    /**
     * Get the vehicle for the service order.
     */
    public function veiculo()
    {
        return $this->belongsTo(Veiculo::class, 'veiculo_id');
    }

    /**
     * Get the user (mechanic) responsible for the service order.
     */
    public function responsavel()
    {
        return $this->belongsTo(User::class, 'responsavel_id');
    }

    /**
     * Get the financial records for the service order.
     */
    public function financeiros()
    {
        return $this->hasMany(Financeiro::class, 'os_id');
    }

    /**
     * Get the quotes for the service order.
     */
    public function orcamentos()
    {
        return $this->hasMany(Orcamento::class, 'os_id');
    }

    /**
     * Get the inspection items for the service order.
     */
    public function vistoriaItens()
    {
        return $this->hasMany(VistoriaItem::class, 'os_id');
    }

    /**
     * Get the executed services for the service order.
     */
    public function servicosExecutados()
    {
        return $this->hasMany(ServicoExecutado::class, 'os_id');
    }
}

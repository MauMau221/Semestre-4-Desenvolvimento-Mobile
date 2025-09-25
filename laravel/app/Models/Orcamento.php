<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Orcamento extends Model
{
    use HasFactory;

    protected $table = 'orcamentos';

    protected $fillable = [
        'os_id',
        'cliente_id',
        'data_criacao',
        'valor_total',
        'status',
    ];

    /**
     * Get the service order for the quote.
     */
    public function ordemServico()
    {
        return $this->belongsTo(OrdemServico::class, 'os_id');
    }

    /**
     * Get the client for the quote.
     */
    public function cliente()
    {
        return $this->belongsTo(Cliente::class);
    }

    /**
     * Get the items for the quote.
     */
    public function itens()
    {
        return $this->hasMany(OrcamentoItem::class);
    }
}

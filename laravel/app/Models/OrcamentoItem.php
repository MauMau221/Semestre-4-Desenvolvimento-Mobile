<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class OrcamentoItem extends Model
{
    use HasFactory;

    protected $table = 'orcamento_itens';

    protected $fillable = [
        'orcamento_id',
        'descricao',
        'quantidade',
        'valor_unitario',
        'valor_total',
    ];

    /**
     * Get the quote that the item belongs to.
     */
    public function orcamento()
    {
        return $this->belongsTo(Orcamento::class);
    }
}

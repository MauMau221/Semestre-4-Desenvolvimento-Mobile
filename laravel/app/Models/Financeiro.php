<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Financeiro extends Model
{
    use HasFactory;

    protected $table = 'financeiro';

    protected $fillable = [
        'os_id',
        'descricao',
        'valor',
        'data',
        'tipo',
        'forma_pagamento',
    ];

    /**
     * Get the service order for the financial record.
     */
    public function ordemServico()
    {
        return $this->belongsTo(OrdemServico::class, 'os_id');
    }
}

<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class VistoriaItem extends Model
{
    use HasFactory;

    protected $table = 'vistoria_itens';

    protected $fillable = [
        'os_id',
        'descricao',
        'status',
    ];

    /**
     * Get the service order that the inspection item belongs to.
     */
    public function ordemServico()
    {
        return $this->belongsTo(OrdemServico::class, 'os_id');
    }
}

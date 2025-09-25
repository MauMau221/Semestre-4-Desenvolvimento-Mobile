<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class ServicoExecutado extends Model
{
    use HasFactory;

    protected $table = 'servicos_executados';

    protected $fillable = [
        'os_id',
        'descricao',
        'status',
        'foto_url',
    ];

    /**
     * Get the service order that the executed service belongs to.
     */
    public function ordemServico()
    {
        return $this->belongsTo(OrdemServico::class, 'os_id');
    }
}

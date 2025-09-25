<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Agendamento extends Model
{
    use HasFactory;

    protected $table = 'agendamentos';

    protected $fillable = [
        'cliente_id',
        'titulo',
        'data',
        'hora',
        'descricao',
    ];

    /**
     * Get the client for the appointment.
     */
    public function cliente()
    {
        return $this->belongsTo(Cliente::class);
    }
}

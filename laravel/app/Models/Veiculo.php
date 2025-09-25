<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Veiculo extends Model
{
    use HasFactory;

    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'garagem';

    protected $fillable = [
        'cliente_id',
        'placa',
        'marca',
        'modelo',
        'ano',
        'cor',
    ];

    /**
     * Get the client that owns the vehicle.
     */
    public function cliente()
    {
        return $this->belongsTo(Cliente::class);
    }
    
    /**
     * Get the service orders for the vehicle.
     */
    public function ordensServico()
    {
        return $this->hasMany(OrdemServico::class, 'veiculo_id');
    }
}

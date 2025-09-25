<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreVeiculoRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'cliente_id' => 'required|exists:clientes,id',
            'placa' => 'required|string|unique:garagem,placa|max:10',
            'marca' => 'required|string|max:50',
            'modelo' => 'required|string|max:50',
            'ano' => 'required|integer|digits:4',
            'cor' => 'required|string|max:30',
        ];
    }
}

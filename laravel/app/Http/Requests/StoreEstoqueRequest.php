<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreEstoqueRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'nome' => 'required|string|max:255',
            'quantidade' => 'required|integer|min:0',
            'preco_custo' => 'required|numeric|min:0',
            'preco_venda' => 'required|numeric|gte:preco_custo',
        ];
    }
}

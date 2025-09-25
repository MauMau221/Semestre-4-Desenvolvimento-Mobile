<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class UpdateEstoqueRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'nome' => 'sometimes|required|string|max:255',
            'quantidade' => 'sometimes|required|integer|min:0',
            'preco_custo' => 'sometimes|required|numeric|min:0',
            'preco_venda' => 'sometimes|required|numeric|gte:preco_custo',
        ];
    }
}

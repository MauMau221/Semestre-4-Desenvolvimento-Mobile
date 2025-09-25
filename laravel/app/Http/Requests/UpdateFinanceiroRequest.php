<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class UpdateFinanceiroRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'os_id' => 'sometimes|nullable|exists:ordem_servicos,id',
            'descricao' => 'sometimes|required|string|max:255',
            'valor' => 'sometimes|required|numeric|min:0',
            'data' => 'sometimes|required|date',
            'tipo' => ['sometimes', 'required', Rule::in(['receita', 'despesa'])],
            'forma_pagamento' => ['sometimes', 'nullable', Rule::in(['Dinheiro', 'Cartão', 'PIX'])],
        ];
    }
}

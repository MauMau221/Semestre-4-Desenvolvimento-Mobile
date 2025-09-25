<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreFinanceiroRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'os_id' => 'nullable|exists:ordem_servicos,id',
            'descricao' => 'required|string|max:255',
            'valor' => 'required|numeric|min:0',
            'data' => 'required|date',
            'tipo' => ['required', Rule::in(['receita', 'despesa'])],
            'forma_pagamento' => ['nullable', Rule::in(['Dinheiro', 'Cartão', 'PIX'])],
        ];
    }
}

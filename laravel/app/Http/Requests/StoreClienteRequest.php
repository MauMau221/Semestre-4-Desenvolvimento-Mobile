<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreClienteRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true; // A autorização será feita pelo middleware na rota
    }

    public function rules(): array
    {
        return [
            'nome' => 'required|string|max:255',
            'cpf_cnpj' => 'required|string|unique:clientes,cpf_cnpj|max:20',
            'telefone' => 'nullable|string|max:20',
            'email' => 'nullable|email|unique:clientes,email|max:255',
            'endereco' => 'nullable|string|max:255',
        ];
    }
}

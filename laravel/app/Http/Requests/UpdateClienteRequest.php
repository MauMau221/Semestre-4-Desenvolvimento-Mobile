<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class UpdateClienteRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        $clienteId = $this->route('cliente')->id;

        return [
            'nome' => 'sometimes|required|string|max:255',
            'cpf_cnpj' => [
                'sometimes',
                'required',
                'string',
                'max:20',
                Rule::unique('clientes')->ignore($clienteId),
            ],
            'telefone' => 'nullable|string|max:20',
            'email' => [
                'nullable',
                'email',
                'max:255',
                Rule::unique('clientes')->ignore($clienteId),
            ],
            'endereco' => 'nullable|string|max:255',
        ];
    }
}

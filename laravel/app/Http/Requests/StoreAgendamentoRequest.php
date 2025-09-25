<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreAgendamentoRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'cliente_id' => 'required|exists:clientes,id',
            'titulo' => 'required|string|max:255',
            'data' => 'required|date',
            'hora' => 'required|date_format:H:i',
            'descricao' => 'nullable|string',
        ];
    }
}

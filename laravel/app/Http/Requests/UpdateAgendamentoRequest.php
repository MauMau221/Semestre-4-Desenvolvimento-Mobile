<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class UpdateAgendamentoRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'cliente_id' => 'sometimes|required|exists:clientes,id',
            'titulo' => 'sometimes|required|string|max:255',
            'data' => 'sometimes|required|date',
            'hora' => 'sometimes|required|date_format:H:i:s,H:i',
            'descricao' => 'nullable|string',
        ];
    }
}

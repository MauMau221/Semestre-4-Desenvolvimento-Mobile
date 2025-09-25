<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class UpdateOrdemServicoRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'cliente_id' => 'sometimes|required|exists:clientes,id',
            'veiculo_id' => 'sometimes|required|exists:garagem,id',
            'responsavel_id' => 'nullable|exists:users,id',
            'data_abertura' => 'sometimes|required|date',
            'data_conclusao' => 'nullable|date|after_or_equal:data_abertura',
            'descricao' => 'sometimes|required|string',
            'status' => 'sometimes|required|string|in:Aberta,Em andamento,Concluída,Entregue,Cancelada',
        ];
    }
}

<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreOrdemServicoRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'cliente_id' => 'required|exists:clientes,id',
            'veiculo_id' => 'required|exists:garagem,id',
            'responsavel_id' => 'nullable|exists:users,id',
            'data_abertura' => 'required|date',
            'data_conclusao' => 'nullable|date|after_or_equal:data_abertura',
            'descricao' => 'required|string',
            'status' => 'nullable|string|in:Aberta,Em andamento,Concluída,Entregue',
        ];
    }
}

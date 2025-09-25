<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class UpdateVeiculoRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        $veiculoId = $this->route('veiculo')->id;

        return [
            'cliente_id' => 'sometimes|required|exists:clientes,id',
            'placa' => [
                'sometimes',
                'required',
                'string',
                'max:10',
                Rule::unique('garagem')->ignore($veiculoId),
            ],
            'marca' => 'sometimes|required|string|max:50',
            'modelo' => 'sometimes|required|string|max:50',
            'ano' => 'sometimes|required|integer|digits:4',
            'cor' => 'sometimes|required|string|max:30',
        ];
    }
}

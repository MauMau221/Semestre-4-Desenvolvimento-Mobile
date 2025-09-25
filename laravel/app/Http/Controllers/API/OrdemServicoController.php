<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\OrdemServico;
use App\Http\Requests\StoreOrdemServicoRequest;
use App\Http\Requests\UpdateOrdemServicoRequest;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class OrdemServicoController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $query = OrdemServico::with(['cliente', 'veiculo', 'responsavel']);

        // Permite filtrar OS por status
        if ($request->has('status')) {
            $query->where('status', $request->status);
        }

        return response()->json($query->latest('data_abertura')->paginate(15));
    }

    public function store(StoreOrdemServicoRequest $request): JsonResponse
    {
        $ordemServico = OrdemServico::create($request->validated());
        return response()->json($ordemServico, 201);
    }

    public function show(OrdemServico $ordemServico): JsonResponse
    {
        // Carrega todos os relacionamentos para a tela de detalhes da OS
        $ordemServico->load([
            'cliente',
            'veiculo',
            'responsavel',
            'orcamentos.itens',
            'vistoriaItens',
            'servicosExecutados'
        ]);
        return response()->json($ordemServico);
    }

    public function update(UpdateOrdemServicoRequest $request, OrdemServico $ordemServico): JsonResponse
    {
        $ordemServico->update($request->validated());
        return response()->json($ordemServico);
    }

    public function destroy(OrdemServico $ordemServico): JsonResponse
    {
        $ordemServico->delete();
        return response()->json(null, 204);
    }
}

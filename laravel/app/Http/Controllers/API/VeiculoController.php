<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Veiculo;
use App\Http\Requests\StoreVeiculoRequest;
use App\Http\Requests\UpdateVeiculoRequest;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class VeiculoController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $query = Veiculo::with('cliente');

        // Permite filtrar veículos por cliente
        if ($request->has('cliente_id')) {
            $query->where('cliente_id', $request->cliente_id);
        }

        return response()->json($query->paginate(15));
    }

    public function store(StoreVeiculoRequest $request): JsonResponse
    {
        $veiculo = Veiculo::create($request->validated());
        return response()->json($veiculo, 201);
    }

    public function show(Veiculo $veiculo): JsonResponse
    {
        $veiculo->load('cliente');
        return response()->json($veiculo);
    }

    public function update(UpdateVeiculoRequest $request, Veiculo $veiculo): JsonResponse
    {
        $veiculo->update($request->validated());
        return response()->json($veiculo);
    }

    public function destroy(Veiculo $veiculo): JsonResponse
    {
        $veiculo->delete();
        return response()->json(null, 204);
    }
}

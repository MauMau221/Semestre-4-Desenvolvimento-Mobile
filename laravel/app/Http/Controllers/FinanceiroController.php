<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Financeiro;
use App\Http\Requests\StoreFinanceiroRequest;
use App\Http\Requests\UpdateFinanceiroRequest;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class FinanceiroController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $query = Financeiro::query();

        // Permite filtrar por tipo (receita/despesa)
        if ($request->has('tipo')) {
            $query->where('tipo', $request->tipo);
        }

        return response()->json($query->latest('data')->paginate(15));
    }

    public function store(StoreFinanceiroRequest $request): JsonResponse
    {
        $financeiro = Financeiro::create($request->validated());
        return response()->json($financeiro, 201);
    }

    public function show(Financeiro $financeiro): JsonResponse
    {
        return response()->json($financeiro);
    }

    public function update(UpdateFinanceiroRequest $request, Financeiro $financeiro): JsonResponse
    {
        $financeiro->update($request->validated());
        return response()->json($financeiro);
    }

    public function destroy(Financeiro $financeiro): JsonResponse
    {
        $financeiro->delete();
        return response()->json(null, 204);
    }
}

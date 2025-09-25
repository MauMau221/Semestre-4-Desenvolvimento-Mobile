<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Estoque;
use App\Http\Requests\StoreEstoqueRequest;
use App\Http\Requests\UpdateEstoqueRequest;
use Illuminate\Http\JsonResponse;

class EstoqueController extends Controller
{
    public function index(): JsonResponse
    {
        return response()->json(Estoque::paginate(15));
    }

    public function store(StoreEstoqueRequest $request): JsonResponse
    {
        $estoque = Estoque::create($request->validated());
        return response()->json($estoque, 201);
    }

    public function show(Estoque $estoque): JsonResponse
    {
        return response()->json($estoque);
    }

    public function update(UpdateEstoqueRequest $request, Estoque $estoque): JsonResponse
    {
        $estoque->update($request->validated());
        return response()->json($estoque);
    }

    public function destroy(Estoque $estoque): JsonResponse
    {
        $estoque->delete();
        return response()->json(null, 204);
    }
}

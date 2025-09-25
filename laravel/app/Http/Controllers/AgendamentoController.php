<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Agendamento;
use App\Http\Requests\StoreAgendamentoRequest;
use App\Http\Requests\UpdateAgendamentoRequest;
use Illuminate\Http\JsonResponse;

class AgendamentoController extends Controller
{
    public function index(): JsonResponse
    {
        return response()->json(Agendamento::with('cliente')->latest('data')->paginate(15));
    }

    public function store(StoreAgendamentoRequest $request): JsonResponse
    {
        $agendamento = Agendamento::create($request->validated());
        return response()->json($agendamento, 201);
    }

    public function show(Agendamento $agendamento): JsonResponse
    {
        $agendamento->load('cliente');
        return response()->json($agendamento);
    }

    public function update(UpdateAgendamentoRequest $request, Agendamento $agendamento): JsonResponse
    {
        $agendamento->update($request->validated());
        return response()->json($agendamento);
    }

    public function destroy(Agendamento $agendamento): JsonResponse
    {
        $agendamento->delete();
        return response()->json(null, 204);
    }
}

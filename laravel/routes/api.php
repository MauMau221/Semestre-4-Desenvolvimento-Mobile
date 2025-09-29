<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\API\AuthController;
use App\Http\Controllers\API\ClienteController;
use App\Http\Controllers\API\VeiculoController;
use App\Http\Controllers\API\OrdemServicoController;
use App\Http\Controllers\API\EstoqueController;
use App\Http\Controllers\API\AgendamentoController;
use App\Http\Controllers\API\FinanceiroController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
*/

Route::post('login', [AuthController::class, 'login']);

// Grupo de rotas protegidas que exigem um token JWT válido
Route::middleware('auth:api')->group(function () {
    Route::post('logout', [AuthController::class, 'logout']);
    Route::post('me', [AuthController::class, 'me']);

    Route::apiResource('clientes', ClienteController::class);
    Route::apiResource('veiculos', VeiculoController::class);
    Route::apiResource('ordens-servico', OrdemServicoController::class);
    Route::apiResource('estoque', EstoqueController::class);
    Route::apiResource('agendamentos', AgendamentoController::class);
    Route::apiResource('financeiro', FinanceiroController::class);
});


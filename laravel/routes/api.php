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
// Adicione aqui os outros controllers que criar (Orcamento, Vistoria, etc.)

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

// Rotas de Autenticação (públicas e para gerenciamento de token)
Route::group([
    'middleware' => 'api',
    'prefix' => 'auth'
], function ($router) {
    Route::post('login', [AuthController::class, 'login']);
    Route::post('logout', [AuthController::class, 'logout']);
    Route::post('refresh', [AuthController::class, 'refresh']);
    Route::post('me', [AuthController::class, 'me']);
});


// Rotas Protegidas que exigem um token JWT válido
Route::middleware('auth:api')->group(function () {
    
    // Rota para obter usuário logado (alternativa ao /auth/me)
    Route::get('/user', function (Request $request) {
        return $request->user();
    });

    // Endpoints para os recursos principais da aplicação
    Route::apiResource('clientes', ClienteController::class);
    Route::apiResource('veiculos', VeiculoController::class);
    Route::apiResource('ordens-servico', OrdemServicoController::class);
    Route::apiResource('estoque', EstoqueController::class);
    Route::apiResource('agendamentos', AgendamentoController::class);
    Route::apiResource('financeiro', FinanceiroController::class);
    // Route::apiResource('orcamentos', OrcamentoController::class); // Descomente quando criar o controller
    
    // TODO: Adicionar rotas para os sub-recursos (itens de vistoria, serviços, etc.)
    // Exemplo de como seriam as rotas aninhadas:
    // Route::apiResource('ordens-servico.vistoria-itens', VistoriaItemController::class)->shallow();
});

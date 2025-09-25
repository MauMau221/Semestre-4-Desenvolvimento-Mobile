<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use App\Models\User;
use App\Models\Cliente;
use App\Models\Veiculo;
use App\Models\OrdemServico;
use App\Models\Estoque;
use App\Models\Agendamento;
use App\Models\Orcamento;
use App\Models\OrcamentoItem;
use App\Models\Financeiro;
use App\Models\VistoriaItem;
use App\Models\ServicoExecutado;
use Carbon\Carbon;

class OficinaSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run(): void
    {
        // Desabilita a verificação de chaves estrangeiras para limpeza
        DB::statement('SET FOREIGN_KEY_CHECKS=0');

        // Limpa as tabelas usando DELETE em vez de TRUNCATE para evitar problemas de permissão
        Financeiro::query()->delete();
        ServicoExecutado::query()->delete();
        VistoriaItem::query()->delete();
        OrcamentoItem::query()->delete();
        Orcamento::query()->delete();
        Agendamento::query()->delete();
        Estoque::query()->delete();
        OrdemServico::query()->delete();
        Veiculo::query()->delete();
        Cliente::query()->delete();
        User::query()->delete();

        // Habilita a verificação de chaves estrangeiras novamente
        DB::statement('SET FOREIGN_KEY_CHECKS=1');

        // --- 1. USUÁRIOS ---
        $userAdmin = User::create([
            'name' => 'Admin Teste',
            'email' => 'admin@oficina.com',
            'password' => Hash::make('senha123'),
        ]);

        $userMecanico = User::create([
            'name' => 'Mecânico João',
            'email' => 'joao@oficina.com',
            'password' => Hash::make('senha123'),
        ]);

        // --- 2. ESTOQUE ---
        $oleo = Estoque::create(['nome' => 'Óleo Motor 5W30 Sintético', 'quantidade' => 50, 'preco_custo' => 35.0, 'preco_venda' => 55.0]);
        $filtroOleo = Estoque::create(['nome' => 'Filtro de Óleo', 'quantidade' => 30, 'preco_custo' => 15.0, 'preco_venda' => 25.0]);
        $pastilha = Estoque::create(['nome' => 'Pastilha de Freio Dianteira', 'quantidade' => 20, 'preco_custo' => 80.0, 'preco_venda' => 120.0]);

        // --- 3. CLIENTES E SEUS DADOS ---

        // Cliente 1: Com OS Concluída
        $cliente1 = Cliente::create(['nome' => 'Carlos Silva', 'cpf_cnpj' => '111.222.333-44', 'telefone' => '11988887777', 'email' => 'carlos.silva@example.com', 'endereco' => 'Rua das Flores, 123']);
        $veiculo1 = Veiculo::create(['cliente_id' => $cliente1->id, 'placa' => 'ABC-1234', 'marca' => 'Fiat', 'modelo' => 'Palio', 'ano' => 2018, 'cor' => 'Prata']);
        $os1 = OrdemServico::create([
            'cliente_id' => $cliente1->id,
            'veiculo_id' => $veiculo1->id,
            'responsavel_id' => $userMecanico->id,
            'data_abertura' => Carbon::now()->subDays(5),
            'data_conclusao' => Carbon::now()->subDays(2),
            'descricao' => 'Cliente solicitou troca de óleo e filtro.',
            'status' => 'Concluída',
        ]);
        VistoriaItem::create(['os_id' => $os1->id, 'descricao' => 'Nível do óleo', 'status' => 'Substituído']);
        $orcamento1 = Orcamento::create(['cliente_id' => $cliente1->id, 'os_id' => $os1->id, 'data_criacao' => $os1->data_abertura, 'status' => 'Aprovado', 'valor_total' => 155.0]);
        OrcamentoItem::create(['orcamento_id' => $orcamento1->id, 'descricao' => $oleo->nome, 'quantidade' => 1, 'valor_unitario' => $oleo->preco_venda]);
        OrcamentoItem::create(['orcamento_id' => $orcamento1->id, 'descricao' => $filtroOleo->nome, 'quantidade' => 1, 'valor_unitario' => $filtroOleo->preco_venda]);
        OrcamentoItem::create(['orcamento_id' => $orcamento1->id, 'descricao' => 'Mão de obra', 'quantidade' => 1, 'valor_unitario' => 75.0]);
        ServicoExecutado::create(['os_id' => $os1->id, 'descricao' => 'Troca de óleo e filtro realizada com sucesso.', 'status' => 'Concluído']);
        Financeiro::create(['os_id' => $os1->id, 'descricao' => "Receita da OS #{$os1->id}", 'valor' => $orcamento1->valor_total, 'data' => $os1->data_conclusao, 'tipo' => 'receita', 'forma_pagamento' => 'PIX']);


        // Cliente 2: Com OS Em Andamento
        $cliente2 = Cliente::create(['nome' => 'Mariana Costa', 'cpf_cnpj' => '222.333.444-55', 'telefone' => '21977776666', 'email' => 'mariana.costa@example.com', 'endereco' => 'Avenida Principal, 456']);
        $veiculo2 = Veiculo::create(['cliente_id' => $cliente2->id, 'placa' => 'DEF-5678', 'marca' => 'Volkswagen', 'modelo' => 'Gol', 'ano' => 2020, 'cor' => 'Branco']);
        $os2 = OrdemServico::create([
            'cliente_id' => $cliente2->id,
            'veiculo_id' => $veiculo2->id,
            'responsavel_id' => $userMecanico->id,
            'data_abertura' => Carbon::now()->subDay(),
            'descricao' => 'Veículo com ruído agudo ao frear.',
            'status' => 'Em andamento',
        ]);
        VistoriaItem::create(['os_id' => $os2->id, 'descricao' => 'Pastilhas de freio dianteiras', 'status' => 'Com problema']);
        $orcamento2 = Orcamento::create(['cliente_id' => $cliente2->id, 'os_id' => $os2->id, 'data_criacao' => $os2->data_abertura, 'status' => 'Aguardando Aprovação', 'valor_total' => 220.0]);
        OrcamentoItem::create(['orcamento_id' => $orcamento2->id, 'descricao' => $pastilha->nome, 'quantidade' => 1, 'valor_unitario' => $pastilha->preco_venda]);
        OrcamentoItem::create(['orcamento_id' => $orcamento2->id, 'descricao' => 'Mão de obra', 'quantidade' => 1, 'valor_unitario' => 100.0]);
        ServicoExecutado::create(['os_id' => $os2->id, 'descricao' => 'Análise do sistema de freios.', 'status' => 'Em execução']);


        // Cliente 3: Com OS Aberta
        $cliente3 = Cliente::create(['nome' => 'Pedro Almeida', 'cpf_cnpj' => '333.444.555-66', 'telefone' => '31966665555', 'email' => 'pedro.almeida@example.com', 'endereco' => 'Praça da Matriz, 789']);
        $veiculo3 = Veiculo::create(['cliente_id' => $cliente3->id, 'placa' => 'GHI-9012', 'marca' => 'Chevrolet', 'modelo' => 'Onix', 'ano' => 2022, 'cor' => 'Vermelho']);
        OrdemServico::create([
            'cliente_id' => $cliente3->id,
            'veiculo_id' => $veiculo3->id,
            'responsavel_id' => $userAdmin->id,
            'data_abertura' => Carbon::now(),
            'descricao' => 'Cliente relata barulho na suspensão dianteira ao passar em buracos.',
            'status' => 'Aberta',
        ]);


        // Cliente 4: Com Agendamento Futuro
        $cliente4 = Cliente::create(['nome' => 'Ana Souza', 'cpf_cnpj' => '444.555.666-77', 'telefone' => '41955554444', 'email' => 'ana.souza@example.com', 'endereco' => 'Alameda dos Anjos, 101']);
        Veiculo::create(['cliente_id' => $cliente4->id, 'placa' => 'JKL-3456', 'marca' => 'Ford', 'modelo' => 'Ka', 'ano' => 2019, 'cor' => 'Preto']);
        Agendamento::create([
            'cliente_id' => $cliente4->id,
            'titulo' => 'Revisão de 40.000km',
            'data' => Carbon::now()->addDays(3),
            'hora' => '10:00:00',
            'descricao' => 'Realizar a revisão completa conforme manual.'
        ]);


        // Cliente 5: Apenas Cadastrado
        $cliente5 = Cliente::create(['nome' => 'Lucas Ferreira', 'cpf_cnpj' => '555.666.777-88', 'telefone' => '51944443333', 'email' => 'lucas.ferreira@example.com', 'endereco' => 'Rua do Comércio, 212']);
        Veiculo::create(['cliente_id' => $cliente5->id, 'placa' => 'MNO-7890', 'marca' => 'Hyundai', 'modelo' => 'HB20', 'ano' => 2021, 'cor' => 'Azul']);


        // --- 4. LANÇAMENTOS FINANCEIROS (DESPESAS) ---
        Financeiro::create(['descricao' => 'Pagamento conta de luz', 'valor' => -450.75, 'data' => Carbon::now()->subDays(1), 'tipo' => 'despesa']);
        Financeiro::create(['descricao' => 'Compra de lote de filtros', 'valor' => -850.00, 'data' => Carbon::now()->subDays(3), 'tipo' => 'despesa']);
    }
}


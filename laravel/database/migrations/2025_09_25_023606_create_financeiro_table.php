<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('financeiro', function (Blueprint $table) {
            $table->id();
            $table->foreignId('os_id')->nullable()->constrained('ordem_servicos')->onDelete('set null');
            $table->string('descricao');
            $table->double('valor');
            $table->date('data');
            $table->string('tipo'); // 'receita' ou 'despesa'
            // NOVO CAMPO: Registra a forma de pagamento conforme a tela de pagamento.
            $table->string('forma_pagamento')->nullable(); // Ex: Dinheiro, Cartão, PIX
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('financeiro');
    }
};

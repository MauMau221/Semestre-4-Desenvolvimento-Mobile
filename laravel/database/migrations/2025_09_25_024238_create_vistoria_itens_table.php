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
        // Tabela para armazenar os itens do checklist da vistoria de uma OS.
        Schema::create('vistoria_itens', function (Blueprint $table) {
            $table->id();
            $table->foreignId('os_id')->constrained('ordem_servicos')->onDelete('cascade');
            $table->string('descricao'); // Ex: Pneus, Óleo, Faróis
            $table->string('status'); // Ex: OK, Com problema, Substituído
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('vistoria_itens');
    }
};

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
        // Tabela para registrar os serviços realizados e o upload de fotos.
        Schema::create('servicos_executados', function (Blueprint $table) {
            $table->id();
            $table->foreignId('os_id')->constrained('ordem_servicos')->onDelete('cascade');
            $table->string('descricao');
            $table->string('status'); // Ex: Pendente, Em execução, Concluído
            $table->string('foto_url')->nullable(); // Armazena o caminho da foto (antes/depois)
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('servicos_executados');
    }
};

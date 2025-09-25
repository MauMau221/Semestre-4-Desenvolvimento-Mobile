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
        Schema::create('ordem_servicos', function (Blueprint $table) {
            $table->id();
            $table->foreignId('cliente_id')->constrained('clientes')->onDelete('cascade');
            $table->foreignId('veiculo_id')->constrained('garagem')->onDelete('cascade');
            $table->foreignId('responsavel_id')->nullable()->constrained('users')->onDelete('set null');
            $table->date('data_abertura');
            $table->date('data_conclusao')->nullable();
            $table->text('descricao');
            $table->string('status')->default('Aberta');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('ordem_servicos');
    }
};

# 🚀 Backend Laravel - API de Autenticação

Backend para aplicação mobile com autenticação usando Laravel Sanctum.

## 📋 Pré-requisitos

- PHP 8.2+
- Composer
- MySQL/SQLite
- Node.js (opcional)

## ⚡ Instalação Rápida

### 1. Clone e instale dependências
```bash
git clone <seu-repositorio>
cd laravel
composer install
composer require tymon/jwt-auth
php artisan vendor:publish --provider="Tymon\\JWTAuth\\Providers\\LaravelServiceProvider"
php artisan jwt:secret

```

### 2. Configure o ambiente
```bash
# Copie o arquivo de configuração
cp .env.example .env

# Gere a chave da aplicação
php artisan key:generate
```

### 3. Configure o banco de dados
Edite o arquivo `.env` com suas credenciais:
```env
DB_CONNECTION=mysql
DB_HOST=seu-host
DB_PORT=3306
DB_DATABASE=seu-banco
DB_USERNAME=seu-usuario
DB_PASSWORD=sua-senha
```

### 4. Execute as migrações
```bash
php artisan migrate
```

### 5. Inicie o servidor
```bash
php artisan serve --host=0.0.0.0 --port=8000
```

## 🔗 Endpoints da API

### Autenticação
- `POST /api/register` - Registro de usuário
- `POST /api/login` - Login de usuário
- `POST /api/logout` - Logout de usuário
- `GET /api/user` - Dados do usuário (protegida)

### Exemplo de uso
```bash
# Registro
curl -X POST http://localhost:8000/api/register \
  -H "Content-Type: application/json" \
  -d '{"name":"João","email":"joao@test.com","password":"12345678","password_confirmation":"12345678"}'

# Login
curl -X POST http://localhost:8000/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@test.com","password":"12345678"}'
```

## 🛠️ Tecnologias

- **Laravel 12** - Framework PHP
- **Laravel Sanctum** - Autenticação por tokens
- **Laravel Breeze** - Sistema de autenticação
- **MySQL** - Banco de dados

## 📱 Para Aplicação Mobile

Use os tokens retornados no login para autenticar requisições:
```http
Authorization: Bearer SEU_TOKEN_AQUI
```

## 🔧 Comandos Úteis

```bash
# Limpar cache
php artisan config:clear
php artisan cache:clear

# Ver status das migrações
php artisan migrate:status

# Executar testes
php artisan test
```

## 📞 Suporte

Para dúvidas sobre a implementação, consulte a documentação do Laravel Sanctum.

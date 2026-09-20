# remedios-service

Microsserviço de **controle de medicamentos** — estoque, validade, consumo e sintomas.

## Stack

| Item | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Cloud | 2025.1.3 |

| Porta | Context path | Perfil exigido |
|---|---|---|
| 9006 | `/remedios-service/` | `ROLE_REMEDIOS` |

## Endpoints

### `/remedios`

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/remedios` | lista os remédios |
| `POST` | `/remedios` | cadastra |
| `GET` | `/remedios/{id}` | busca por id |
| `PUT` | `/remedios/{id}` | atualiza |
| `DELETE` | `/remedios/{id}` | remove |
| `GET` | `/remedios/estoque-baixo` | remédios com estoque abaixo do mínimo |
| `GET` | `/remedios/vencendo` | remédios próximos do vencimento |
| `PUT` | `/remedios/consumir/{id}` | registra consumo e baixa o estoque |

### `/sintomas`

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/sintomas` | lista os sintomas |
| `POST` | `/sintomas` | cadastra |
| `GET` | `/sintomas/{id}` | busca por id |
| `PUT` | `/sintomas/{id}` | atualiza |
| `DELETE` | `/sintomas/{id}` | remove |

## Autenticação

As requisições precisam do token JWT emitido pelo `autenticacao-service`, no cabeçalho:

```
Authorization: Bearer <token>
```

O serviço apenas **valida** o token — ele não emite nenhum. A chave de validação vem de `JWTSecret`, em `secret/application` no Vault, e precisa ser a mesma usada pelo emissor.

> **Atenção ao segredo:** desde a migração para o jjwt 0.13, `JWTSecret` precisa ser uma string **Base64** que decodifique para **no mínimo 64 bytes** — exigência do HS512. Gere um com `openssl rand -base64 64`. Se o valor não atender, a aplicação falha no startup com mensagem explícita, em vez de aceitar uma chave fraca em silêncio.

## Banco de dados

PostgreSQL, com schema versionado por **Flyway** (migrations em `src/main/resources/db/migration`):

- `V001__Inicial.sql`

As migrations rodam automaticamente no startup.

> No Spring Boot 4 a autoconfiguração do Flyway passou a viver no módulo `spring-boot-flyway`. Sem essa dependência o Flyway é ignorado **em silêncio** — a aplicação sobe normalmente e nenhuma migration é aplicada. Ela está declarada no `pom.xml`; não remova.

**Entidades:** `Remedio`, `Consumo`, `Sintoma`.

## Configuração

A aplicação não guarda configuração própria: ela busca tudo no arranque, via `spring.config.import`.

| Origem | O que vem de lá |
|---|---|
| **Vault** (`secret/application`) | segredos compartilhados: `JWTSecret`, credenciais de e-mail, AWS, Eureka |
| **Vault** (`secret/<nome-do-serviço>`) | segredos próprios, como as credenciais do banco |
| **Config Server** | `server.port`, `context-path`, datasource e demais propriedades |

### Variável de ambiente obrigatória

| Variável | Para que serve |
|---|---|
| `VAULT_TOKEN` | token de acesso ao Vault |

`VAULT_TOKEN` **não tem valor padrão**. Sem ela, o Spring envia a string literal `${VAULT_TOKEN}` ao Vault, recebe `403` e — como `spring.cloud.vault.fail-fast` vem desligado — o erro só aparece bem depois, disfarçado de placeholder não resolvido (`${...} is malformed`). Se quiser que a falha apareça na hora, ligue `spring.cloud.vault.fail-fast: true`.

Também são necessários `VAULT_HOST`, `VAULT_PORT` e `VAULT_SCHEME` quando o Vault não está em `localhost:8200` via `http`, e `CONFIG_SERVER_USER` / `CONFIG_SERVER_PASS` nos serviços que leem do Config Server.

## Como executar

```bash
# build
./mvnw clean package

# execução
VAULT_TOKEN=<seu-token> java -jar target/remedios-service-*.jar --spring.profiles.active=dev
```

> **Dependências no ar:** este serviço só sobe com o **Vault**, o **Config Server** e o **Eureka** disponíveis, além do seu banco PostgreSQL.

A aplicação sobe em `http://localhost:9006/remedios-service/`.

### Docker

O `Dockerfile` espera o jar já na raiz do projeto, com o nome `sistema-remedios-service.jar`:

```bash
./mvnw clean package
cp target/remedios-service-*.jar sistema-remedios-service.jar

docker build \
  --build-arg VAULT_HOST=<host> \
  --build-arg VAULT_TOKEN=<token> \
  --build-arg CONFIG_SERVER_USER=<usuario> \
  --build-arg CONFIG_SERVER_PASS=<senha> \
  -t remedios-service .
```

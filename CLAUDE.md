# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Purpose

DASN Simei é um projeto de estudos para declaração de faturamento do MEI (Simples Nacional). Importa extratos bancários em CSV, filtra transações positivas e gera totalizadores anuais, trimestrais e mensais.

## Commands

```bash
# Compilar e rodar todos os testes
mvn test

# Rodar um único teste por nome
mvn test -Dtest=MovimentoServiceTest#totalAnual_deveRetornarSomaCorretaDoRepositorio

# Rodar todos os testes de uma classe
mvn test -Dtest=MovimentoServiceTest

# Build sem testes
mvn package -DskipTests

# Iniciar a aplicação (requer banco MySQL rodando)
mvn spring-boot:run
```

A aplicação sobe na porta padrão 8080. Swagger UI disponível em `http://localhost:8080`.

## Environment

O banco de dados é configurado via variáveis de ambiente (`DB_URL`, `DB_USER`, `DB_PASS`). Localmente, essas variáveis ficam em `databaseInfo.yml` (ignorado pelo git). Sem essas variáveis o contexto Spring não sobe — testes unitários puros (sem `@SpringBootTest`) não são afetados.

## Architecture

### Camadas

```
Controller → Service → Repository → MySQL (via JPA/Flyway)
```

- **controllers/**: handlers REST; a lógica de documentação OpenAPI fica em `controllers/docs/` como interfaces separadas que os controllers implementam.
- **services/**: toda a regra de negócio, incluindo o parsing de CSV.
- **repositories/**: Spring Data JPA com queries JPQL customizadas para os totalizadores.
- **model/entities/**: hierarquia de entidades com Single-Table Inheritance.
- **dtos/ + mappers/**: conversão de entidade para DTO (somente entity → DTO, sem mapeamento inverso).

### Hierarquia de entidades (Single-Table Inheritance)

Tabela única `movimento_financeiro` com coluna discriminadora `tipo_movimento`:

| Discriminador | Classe | Campos extras |
|---|---|---|
| `MV` | `Movimento` | `data`, `identificadorTransacao` |
| `VE` | `VendaExterna` | — |
| `PR` | `Previsao` | — |

`VendaExterna` e `Previsao` não possuem data — representam previsões e ativos, não transações bancárias.

### Processamento de CSV

O `MovimentoService.processarArquivoCSV()` detecta o tipo pelo prefixo do nome do arquivo:

| Prefixo | Tipo gerado | Observação |
|---|---|---|
| `nu` | `Movimento` | Pula a primeira linha (cabeçalho) |
| `si` | `Movimento` | Sem cabeçalho |
| `pr` | `Previsao` | Sem cabeçalho |
| `ve` | `VendaExterna` | Sem cabeçalho |

Apenas valores positivos (não negativos) são importados para `Movimento`.

### Queries do repositório

O `MovimentoFinanceiroRepository` usa `type(m)` do JPQL para filtrar por subclasse — não há campo de categoria no DTO. A query de categoria recebe `"vendas"` ou `"previsao"` como string e mapeia internamente para `VendaExterna` e `Previsao`.

## Tests

Testes unitários usam `@ExtendWith(MockitoExtension.class)` sem contexto Spring. O `@Mock` vai na dependência da camada testada; o `@InjectMocks` vai na classe sob teste.

O Surefire está configurado com `-javaagent` apontando para o `mockito-core` e `-Xshare:off` para compatibilidade com Java 25.

Cada teste deve declarar seus próprios stubs (`when(...).thenReturn(...)`) — não usar `@BeforeEach` para stubs a menos que **todos** os testes da classe precisem do mesmo comportamento, pois o Mockito no modo estrito lança `UnnecessaryStubbingException` para stubs não utilizados.
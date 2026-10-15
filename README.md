# PortalBillion API

API de estudo para cadastro e consulta de clientes, desenvolvida em Java com Spring Boot. O objetivo é aprender a construção de uma API REST em etapas, aplicando separação de responsabilidades, nomes claros e injeção por construtor.

O back-end é separado do front-end do PortalBillion. A implementação usa a tabela `clients` como referência de nomenclatura, com apenas parte dos atributos nesta etapa. Não é uma reprodução completa do banco original.

## Estado atual

| Funcionalidade | Estado |
|---|---|
| Listar clientes | Implementada |
| Cadastrar cliente | Implementada |
| Validar campos obrigatórios | Implementada |
| Gerar identificador UUID | Implementada |
| Testar operações pelo Swagger UI | Disponível |
| Consultar cliente por ID | Pendente |
| Alterar cliente | Pendente |
| Excluir cliente | Pendente |

## Tecnologias

Versões e dependências declaradas no projeto:

| Tecnologia | Versão ou função |
|---|---|
| Java | 25 |
| Spring Boot | 4.1.1 |
| Gradle Wrapper | 9.8.1 |
| Spring Web MVC | Controllers e comunicação HTTP |
| Spring Data JPA / Hibernate ORM | Persistência e mapeamento das entidades |
| Spring Boot Validation | Validação com Jakarta Validation |
| H2 e H2 Console | Banco em memória e console de consultas |
| springdoc-openapi-starter-webmvc-ui | 3.1.0; documentação e testes HTTP pelo Swagger UI |
| Spring Boot DevTools | Recursos de desenvolvimento |
| Lombok | Declarado; as classes atuais usam getters e setters explícitos |
| Starters de teste JPA e Web MVC | Suporte a testes |
| JUnit Platform | Execução dos testes |

As versões das dependências gerenciadas pelo Spring Boot não são fixadas individualmente no `build.gradle`.

## Organização do código

Pacote base: `org.example.portalbillion_api`.

```text
src/main/java/org/example/portalbillion_api/
├── PortalBillionApiApplication.java
├── controller/
│   └── ClienteController.java
├── service/
│   └── ClienteService.java
├── repository/
│   └── ClienteRepository.java
└── model/
    └── Cliente.java

src/main/resources/
└── application.properties
```

O fluxo de uma operação é:

```text
Swagger UI ou front-end
    → ClienteController
    → ClienteService
    → ClienteRepository
    → H2
```

- **Controller:** recebe requisições HTTP, aciona validações e devolve respostas JSON.
- **Service:** coordena as operações de clientes. No cadastro, cria um novo objeto e copia somente os campos permitidos, sem aproveitar o ID enviado na entrada.
- **Repository:** estende `JpaRepository<Cliente, UUID>` e fornece as operações de persistência. O Spring Data disponibiliza sua implementação.
- **Model:** contém a entidade `Cliente`, seu mapeamento para `clients` e as restrições de validação.

O controller recebe o service pelo construtor, e o service recebe o repository da mesma forma. O Spring fornece essas dependências ao criar os objetos. Os campos `final` impedem a reatribuição dessas referências.

## Como executar

Pré-requisitos: JDK 25 configurado e acesso à internet para o primeiro download do Gradle e das dependências. Não é necessário instalar um servidor H2 separado.

Na IDE, abra a pasta que contém `build.gradle`, sincronize o projeto Gradle e execute `PortalBillionApiApplication`.

Como alternativa, no PowerShell, a partir da raiz do projeto:

```powershell
.\gradlew.bat bootRun
```

Com a configuração atual, a aplicação usa o endereço base `http://localhost:8080`.

| Recurso | Endereço |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Descrição OpenAPI | http://localhost:8080/v3/api-docs |
| Console H2 | http://localhost:8080/h2-console |

## Banco H2

Configuração atual em `application.properties`:

```properties
spring.application.name=PortalBillion_API

spring.datasource.url=jdbc:h2:mem:portalbillion
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Para conectar pelo console, use JDBC URL `jdbc:h2:mem:portalbillion`, usuário `sa` e senha vazia.

O banco fica em memória. Os dados não persistem entre execuções da aplicação. O Hibernate cria o esquema a partir das entidades; `create-drop` também determina a remoção do esquema no encerramento normal. O console H2 é destinado ao desenvolvimento.

Exemplo de consulta no console:

```sql
SELECT * FROM clients;
```

## Modelo de cliente

| Atributo Java | Coluna | Tipo Java | Regra atual |
|---|---|---|---|
| `id` | `id` | `UUID` | Chave primária gerada com `GenerationType.UUID` |
| `legalName` | `legal_name` | `String` | `@NotBlank` |
| `cnpj` | `cnpj` | `String` | `@NotBlank` |
| `taxRegime` | `tax_regime` | `String` | `@NotBlank` |
| `status` | `status` | `Boolean` | `@NotNull`; convenção atual: ativo/inativo |

`@NotBlank` rejeita `null`, texto vazio e texto composto apenas de espaços. `@NotNull` rejeita somente `null`; portanto, `false` é um status aceito.

Limitações desta etapa:

- O CNPJ é validado apenas quanto ao preenchimento. Não há validação de formato, dígitos verificadores ou unicidade.
- `taxRegime` aceita qualquer texto não vazio; ainda não existe uma lista de valores permitidos.
- O modelo não inclui os demais atributos nem os relacionamentos do banco de referência.
- A entidade é usada como entrada e saída HTTP; ainda não existem DTOs no projeto.
- As respostas de erro ainda não têm tratamento personalizado.
- Autenticação e integração com o front-end ainda não foram implementadas.

## Operações implementadas

### Listar clientes

```http
GET /clientes
```

Retorna HTTP `200` e uma lista JSON. Quando não existem registros:

```json
[]
```

### Cadastrar cliente

```http
POST /clientes
Content-Type: application/json
```

Corpo de exemplo, com dados fictícios:

```json
{
  "legalName": "Empresa de Aprendizado Ltda",
  "cnpj": "00000000000000",
  "taxRegime": "SIMPLES_NACIONAL",
  "status": true
}
```

O CNPJ acima serve somente para o exercício de preenchimento e não representa um documento válido. `SIMPLES_NACIONAL` é um texto de exemplo, sem validação por enum nesta etapa.

Não é necessário enviar `id`. O cadastro copia os campos para uma nova entidade e o identificador é gerado ao persistir.

Em caso de sucesso, retorna HTTP `201` e o cliente salvo, incluindo o UUID gerado. Violações das restrições de entrada acionadas por `@Valid` retornam HTTP `400` por padrão.

## Verificação manual pelo Swagger

1. Inicie a aplicação e abra o Swagger UI.
2. Execute `GET /clientes` e confira HTTP `200`.
3. Em `POST /clientes`, clique em **Try it out**, informe o JSON e clique em **Execute**.
4. Confira HTTP `201` e o UUID gerado.
5. Execute novamente `GET /clientes` e confira o registro.
6. Tente cadastrar com `legalName` igual a `"   "` e confira HTTP `400`.
7. Tente cadastrar com `status` igual a `null` e confira HTTP `400`.

Faça cadastro e listagem na mesma execução, pois o banco é temporário. O Swagger envia requisições HTTP; consultas SQL diretas são feitas pelo console H2.

O cadastro, a listagem e o teste de validação da razão social foram relatados como bem-sucedidos pelo autor durante o aprendizado. Os demais passos são sugestões de verificação.

## Testes automatizados

O projeto contém o teste inicial `contextLoads()`, anotado com `@SpringBootTest`. Ele verifica o carregamento do contexto; não cobre o comportamento das rotas nem as regras de cadastro.

Para executar os testes:

```powershell
.\gradlew.bat test
```

A criação deste README não incluiu execução dos testes automatizados.

## Relação com o projeto de exemplo

Referência: [lgsreal/2tdspw-mvc](https://github.com/lgsreal/2tdspw-mvc).

Conferência realizada em 15/10/2026. A referência pode mudar depois dessa data.

| Aspecto | Exemplo | PortalBillion API |
|---|---|---|
| Java | 25 | 25 |
| Spring Boot | 4.1.0 | 4.1.1 |
| Organização | Controller, service, repository e model; também possui DTO e mapper | Controller, service, repository e model |
| Persistência | Spring Data JPA e H2 | Spring Data JPA e H2 |
| Identificador | UUID gerado pelo JPA | UUID gerado pelo JPA |
| Validação no cadastro | Restrições na entidade e `@Valid` no controller | Mesmo princípio |
| Validação em DTO | Também declarada em `LivroRequest` | DTO ainda não criado |
| Dependências entre classes | Injeção por construtor | Injeção por construtor |
| Resposta | Páginas HTML, Thymeleaf e `BindingResult` | JSON, `@RestController` e validação de `@RequestBody` |
| Segurança | Security e OAuth2 Client declarados | Não adicionados nesta etapa |
| Swagger UI | Não declarado no build consultado | springdoc-openapi 3.1.0 |
| Getters e setters | Escritos explicitamente | Escritos explicitamente; Lombok disponível |

A adaptação preserva a estrutura didática do exemplo e usa HTTP/JSON para um front-end separado. A existência de camadas, por si só, não garante SOLID: cada classe deve manter uma responsabilidade clara, e novas abstrações devem responder a necessidades concretas.

## Próximas etapas de aprendizado

1. Consultar cliente por ID e tratar recurso inexistente com HTTP `404`.
2. Implementar alteração e exclusão.
3. Definir regras de CNPJ, regime tributário e status.
4. Criar DTOs e organizar os erros de validação.
5. Escrever testes de comportamento das operações.
6. Evoluir os atributos e a persistência conforme os requisitos do PortalBillion.

## Documentação de apoio

- [Spring MVC: corpo da requisição e validação](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/requestbody.html)
- [Spring MVC: mapeamento de requisições](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html)
- [Spring: injeção de dependências](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired.html)
- [Spring Boot: bancos SQL e H2](https://docs.spring.io/spring-boot/reference/data/sql.html)
- [springdoc-openapi](https://springdoc.org/getting-started.html)

# MeetingRoom 🏢📅

Sistema de gerenciamento de reservas de salas de reunião, desenvolvido em **Java 21** com **Spring Boot**, como parte do **Desafio Back-End Java - Nível 1 da Alura**.

A aplicação permite cadastrar usuários e salas, e gerenciar reservas com validação de conflitos de horário, capacidade e cancelamento — cobrindo todo o ciclo de uma API REST real: modelagem de domínio, endpoints, persistência e testes automatizados.

---

## 📋 Funcionalidades

- **Usuários**: cadastro, busca por e-mail, atualização e desativação
- **Salas**: cadastro, busca por nome, atualização e desativação
- **Reservas**: criação, listagem paginada, busca por id, atualização e cancelamento
- Validação de **conflito de horários** — a mesma sala não pode ter reservas sobrepostas
- Validação de **capacidade** e de **datas** (início anterior ao fim)
- Reservas canceladas não entram na checagem de conflito

---

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot** (Web, Data JPA, Validation)
- **MySQL** — banco de dados relacional (ambiente de produção)
- **H2 Database** — banco em memória (ambiente de testes)
- **JUnit 5** e **Mockito** — testes unitários
- **MockMvc** — testes de integração dos controllers
- **Lombok** — redução de boilerplate
- **Maven** — gerenciamento de dependências e build

---

## 🗂️ Estrutura do projeto

```
src
├── main
│   ├── java/br/com/meetingroom
│   │   ├── controller     # Endpoints REST
│   │   ├── service        # Regras de negócio
│   │   ├── repository      # Acesso a dados (Spring Data JPA)
│   │   ├── entities        # Entidades JPA (Usuario, Sala, Reserva)
│   │   ├── dtos             # Records de entrada/saída (DTOs)
│   │   └── enums            # TipoSala, StatusReserva
│   └── resources
│       └── application.properties
└── test
    └── java/br/com/meetingroom
        ├── service          # Testes unitários (JUnit + Mockito)
        └── controller       # Testes de integração (MockMvc)
```

---

## 🔗 Endpoints

Todas as rotas são versionadas com o prefixo `/api/v1`.

### Usuários — `/api/v1/usuarios`
| Método | Rota                  | Descrição                     |
|--------|------------------------|--------------------------------|
| GET    | `/`                    | Lista todos os usuários       |
| GET    | `/{email}`             | Busca usuário por e-mail       |
| POST   | `/criar`               | Cria um novo usuário            |
| PUT    | `/{id}`                | Atualiza um usuário              |
| PATCH  | `/{id}/desativar`     | Desativa um usuário              |

### Salas — `/api/v1/salas`
| Método | Rota                  | Descrição                     |
|--------|------------------------|--------------------------------|
| GET    | `/`                    | Lista todas as salas          |
| GET    | `/{nome}`              | Busca sala por nome            |
| POST   | `/criar`               | Cria uma nova sala               |
| PUT    | `/{id}`                | Atualiza uma sala                |
| PATCH  | `/{id}/desativar`     | Desativa uma sala                 |

### Reservas — `/api/v1/reservas`
| Método | Rota      | Descrição                                  |
|--------|-----------|----------------------------------------------|
| GET    | `/`       | Lista reservas de forma paginada            |
| GET    | `/{id}`   | Busca reserva por id                          |
| POST   | `/criar`  | Cria uma nova reserva                          |
| PUT    | `/{id}`   | Atualiza uma reserva                            |
| DELETE | `/{id}`   | Cancela uma reserva                              |

---

## ▶️ Como executar o projeto

### Pré-requisitos
- Java 21
- Maven (ou usar o Maven Wrapper incluso no projeto)
- MySQL rodando localmente (ajuste as credenciais em `application.properties`)

### Passos

```bash
# Clone o repositório
git clone https://github.com/sousaped/MeetingRoom.git
cd MeetingRoom

# Rode a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080/api/v1`.

---

## ✅ Executando os testes

O projeto conta com testes unitários (camada de service) e testes de integração (camada de controller):

```bash
./mvnw test
```

- **Testes unitários**: validam as regras de negócio isoladamente — conflito de horário, capacidade, cancelamento — usando JUnit 5 e Mockito, sem subir o contexto do Spring.
- **Testes de integração**: validam o comportamento real dos endpoints HTTP (roteamento, serialização JSON, validação, status codes) usando `@SpringBootTest` + `@AutoConfigureMockMvc`, com H2 como banco em memória.

---

## 📖 Sobre o desafio

Este projeto foi desenvolvido como parte do **Desafio Back-End Java - Nível 1** da Alura, cobrindo quatro frentes principais:

- Modelagem de domínio e orientação a objetos
- Construção de uma API CRUD com Spring
- Persistência de dados com Spring Data JPA
- Testes de unidade e boas práticas de desenvolvimento

---

## 👤 Autor

**Alex Sousa**
- GitHub: [@sousaped](https://github.com/sousaped)
- LinkedIn: [alex-sousa](https://linkedin.com/in/alex-sousa-80949a231)

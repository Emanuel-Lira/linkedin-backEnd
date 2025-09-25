# LinkedIn Clone - Backend (Projeto Académico)

Este repositório contém o backend de uma aplicação estilo LinkedIn, desenvolvido como um projeto para demonstrar competências em arquitetura de microserviços, integração de bancos de dados relacionais e não relacionais, e segurança de APIs.

## 🚀 Visão Geral da Arquitetura

O projeto foi construído seguindo uma **arquitetura de microserviços**, dividindo as responsabilidades em dois serviços RESTful independentes que se comunicam via HTTP. Esta abordagem utiliza **persistência poliglota**, escolhendo o banco de dados mais adequado para cada tipo de tarefa.

### 1. `core-service` (Java + Spring Boot + MySQL)
O coração transacional da aplicação. Responsável por gerir todos os dados principais e a segurança.
- **Banco de Dados:** MySQL
- **Funcionalidades:** Gestão de Utilizadores, Empresas, Vagas, Posts, Comentários, Mensagens Privadas e Autenticação.

### 2. `people-graph-service` (Java + Spring Boot + Neo4j)
O cérebro social da rede, focado em modelar, criar e consultar os relacionamentos e conexões entre as entidades.
- **Banco de Dados:** Neo4j (Banco de Dados de Grafos)
- **Funcionalidades:** Criação de nós de Utilizadores e Empresas, e modelagem de relacionamentos como `CONNECTED_TO` (conexões entre utilizadores), `FOLLOWS` (seguir empresas) e `WORKED_AT`.

### Bônus: Performance com Cache
- **Redis:** O `core-service` foi aprimorado com Redis para implementar uma camada de cache, melhorando drasticamente a performance de leituras frequentes e reduzindo a carga no banco de dados MySQL.

## ✨ Tecnologias e Conceitos Implementados

- **Linguagem:** Java 17+
- **Framework:** Spring Boot 3
- **Persistência:**
  - Spring Data JPA com Hibernate (MySQL)
  - Spring Data Neo4j (Neo4j)
  - Spring Data Redis (Cache)
- **Segurança:** Spring Security 6 com autenticação baseada em **JWT (JSON Web Tokens)**.
- **Bancos de Dados:**
  - **MySQL** para dados relacionais.
  - **Neo4j** para dados de grafo, ideal para redes sociais e recomendações.
  - **Redis** para cache de alta performance.
- **Infraestrutura:** **Docker** para gerir os containers dos bancos de dados (Neo4j e Redis).
- **API:**
  - RESTful com DTOs (Data Transfer Objects) para segurança e consistência.
  - Paginação e ordenação em endpoints de listagem.
  - Validação de dados de entrada (`Bean Validation`).
- **Ferramentas:** Maven, Postman.

## 📋 Funcionalidades Implementadas

### No `core-service`:
- [✔] Sistema completo de **Registo e Login** com JWT.
- [✔] CRUD completo e seguro para **Utilizadores, Posts, Comentários, Empresas, Vagas e Mensagens Privadas**.
- [✔] Regras de negócio, como permissões (ex: apenas o dono pode apagar o seu post).
- [✔] Comunicação síncrona com o `people-graph-service` para notificar a criação de novos utilizadores e empresas.

### No `people-graph-service`:
- [✔] Criação de **Nós** para `User` e `Company`.
- [✔] Criação de **Relacionamentos** como `CONNECTED_TO`, `FOLLOWS` e `WORKED_AT`.
- [✔] Endpoint de consulta para **sugestão de conexões** (amigos de amigos).

## ⚙️ Como Executar o Projeto

### Pré-requisitos
- [Java (JDK) 17 ou superior](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8 ou superior](https://maven.apache.org/download.cgi)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Postman](https://www.postman.com/downloads/)
- Um cliente MySQL (como MySQL Workbench ou DBeaver).

### Passo a Passo

1.  **Clonar o Repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/linkedin-clone-backend.git](https://github.com/seu-usuario/linkedin-clone-backend.git)
    cd linkedin-clone-backend
    ```

2.  **Subir os Bancos de Dados com Docker:**
    Abra um terminal e execute os seguintes comandos:
    ```bash
    # Iniciar o container do Neo4j (use a sua senha)
    docker run --detach --name linkedin-neo4j -p 7474:7474 -p 7687:7687 --env NEO4J_AUTH=neo4j/sua-senha-aqui neo4j:latest

    # Iniciar o container do Redis
    docker run --detach --name linkedin-redis -p 6379:6379 redis:latest
    ```

3.  **Configurar o `core-service`:**
    - Crie um banco de dados no seu MySQL chamado `linkedin_core_db`.
    - Navegue até a pasta `core-service/src/main/resources/`.
    - Abra o ficheiro `application.properties` e ajuste as credenciais do seu MySQL (`spring.datasource.password`).

4.  **Executar as Aplicações:**
    Abra **dois terminais separados**.

    - **No primeiro terminal (para o `core-service`):**
      ```bash
      cd core-service
      mvn spring-boot:run
      ```
    - **No segundo terminal (para o `people-graph-service`):**
      ```bash
      cd people-graph-service
      mvn spring-boot:run
      ```

    Ao final, o `core-service` estará a rodar na porta `8080` e o `people-graph-service` na porta `8081`.

5.  **Testar com a Coleção Postman:**
    - Importe a coleção Postman (incluída neste repositório) para o seu Postman.
    - Siga o roteiro de testes, começando pelo registo de utilizadores e login para obter o token JWT.

---
Este projeto representa uma jornada completa pelo desenvolvimento de um sistema backend moderno, desde a concepção da arquitetura até a implementação de funcionalidades complexas e seguras.
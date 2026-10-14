# BHV-Transfer - Sistema de Gestão de Vistorias

Interface backend desenvolvida para a centralização e organização de vistorias técnicas de telecomunicações da **Behive Tecnologia**.

---

## 🚀 Como Começar

### Pré-requisitos

Antes de iniciar, você precisará ter instalado em sua máquina:
* **Java 21**
* **Docker Desktop**
* **Maven 3.x**

### Configuração do Ambiente

Siga os passos abaixo para rodar a aplicação localmente:

1.  **Inicie o banco de dados PostgreSQL** via Docker:
    ```bash
    docker-compose up -d
    ```

2.  **Configuração de Caminhos e Links**:
    Certifique-se de configurar a pasta física e a URL dinâmica no arquivo `src/main/resources/application.properties`:
    ```properties
    app.pasta.upload=C:/seu-caminho/uploads
    app.base.url=http://localhost:8080
    ```

3.  **Execute a aplicação**:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 🛠️ Tecnologias Utilizadas

O projeto foi construído utilizando as seguintes tecnologias:

* **Spring Boot 3**: Framework principal para o desenvolvimento da API.
* **Spring Data JPA**: Abstração de persistência de dados.
* **PostgreSQL**: Banco de dados relacional (via Docker).
* **Dropzone.js**: Biblioteca Front-end para processamento de uploads drag-and-drop.
* **JUnit 5 & Mockito**: Ferramentas para garantia de qualidade via testes automatizados unitários.

---

## 📂 Estrutura de Pacotes

A arquitetura do projeto segue o padrão de camadas:

* `domain`: Contém as entidades JPA que representam as tabelas do banco de dados (Site, Tecnico, LoteUpload).
* `repository`: Interfaces para comunicação e acesso aos dados (Spring Data).
* `service`: Camada de lógica de negócio, manipulação de Entrada/Saída (IO) e regras de segurança de sistema de arquivos.
* `controller`: Responsável por expor os endpoints REST da API.

---

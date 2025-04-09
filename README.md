# 📋 Board Project

Bem-vindo ao **Board**, um sistema de gerenciamento de tarefas desenvolvido como parte do bootcamp **Decola Tech 2025** da plataforma **DIO (Digital Innovation One)**. Este projeto foi criado com o objetivo de aprofundar meus conhecimentos em **JPA (Hibernate)**, **lógica de programação** e **testes unitários**, aplicando boas práticas de desenvolvimento backend com **Java** e **Spring Boot**.

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Flyway**
- **PostgreSQL**
- **Apache POI** (exportação de relatórios em Excel)
- **PDFBox** (exportação de relatórios em PDF)
- **JUnit 5**
- **Mockito**
- **Lombok**

---

## 📚 Objetivos do Projeto

✔️ Aprimorar a utilização de JPA com Hibernate  
✔️ Consolidar conceitos de modelagem de dados relacionais  
✔️ Criar lógica de negócios com foco em clareza e organização  
✔️ Praticar a escrita de testes unitários com JUnit e Mockito  
✔️ Gerar relatórios em Excel e PDF  
✔️ Simular interações de um sistema CLI simples

---

## 🧠 Funcionalidades

- Cadastro de cards em um board Kanban
- Alteração de status de cards (movimentação entre colunas)
- Bloqueio e desbloqueio de cards com justificativas
- Relatórios de movimentações
- Relatórios de bloqueios/desbloqueios
- Exportação de relatórios em **.xlsx** e **.pdf**
- Interface por linha de comando (CLI)

---

## 🧪 Testes

O projeto possui cobertura de testes para as principais funcionalidades, com foco em:

- Serviços de negócios
- Exportadores de relatórios
- Validações e lógicas de tempo
- Interação com o console (mockado)

---

## 🗂 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com.gag.board/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── entity/
│   │       ├── repository/
│   │       ├── service/
│   │       └── util/exporter/
│   └── resources/
|       └── db
|            └── migration
│       └── application.properties
│       └── Resource Bundle 'messages'
├── test/
│   └── java/com.gag.board/
│       ├── service/
│       └── util/exporter/
```

---

## 🛠 Como Executar

```bash
# Clone o repositório
git clone https://github.com/GabrielG1997/board.git

# Entre na pasta do projeto
cd board

# Compile o projeto
./mvnw clean install

# Execute o aplicativo 
# (O idioma das mensagens e a data/hora consideram o locale da maquina rodando a JVM)
./mvnw spring-boot:run
```

---

## 💡 Futuras Melhorias

- Implementação de interface web com Angular ou React
- Autenticação e autorização de usuários
- Armazenamento em nuvem para relatórios gerados

---

## 🧑‍💻 Sobre o Autor

Desenvolvido por mim, **Gabriel Alves Guimarães** como parte do meu processo de aprendizado em backend Java.  
Bootcamp: **Decola Tech 2025**  
Plataforma: **[Digital Innovation One](https://dio.me)**
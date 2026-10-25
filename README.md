# 🛠️ Automechanic API

## 📋 Sobre o Projeto

A **Automechanic API** é uma API RESTful desenvolvida para resolver o problema de gestão de oficinas mecânicas. O sistema permite o controle completo de clientes, veículos, catálogo de peças e serviços, além da gestão de Ordens de Serviço (OS) com cálculo automático de valores e controle de itens (peças e mão de obra).

O projeto foi construído aplicando as melhores práticas do ecossistema Spring, como arquitetura em camadas, tratamento global de exceções, uso de DTOs (via entidades) e separação de ambientes (Dev/Test/Prod).

---

## 🚀 Stack Tecnológica

- **Linguagem:** Java 25
- **Framework:** Spring Boot 4
- **Persistência:** Spring Data JPA / Hibernate
- **Bancos de Dados:** PostgreSQL (Produção) e H2 (Testes/Desenvolvimento)
- **Build Tool:** Maven
- **Documentação de API:** Postman (testes manuais)

---
🔌 Endpoints da API
Abaixo estão os endpoints disponíveis na API. Todos os retornos são em formato JSON.

Cliente	GET	/clientes	Lista todos os clientes
GET	/clientes/{id}	Busca cliente por ID
POST	/clientes	Cadastra um novo cliente
PUT	/clientes/{id}	Atualiza um cliente
DELETE	/clientes/{id}	Remove um cliente
Veículo	GET	/veiculos	Lista todos os veículos
GET	/veiculos/{id}	Busca veículo por ID
POST	/veiculos	Cadastra um novo veículo
PUT	/veiculos/{id}	Atualiza um veículo
DELETE	/veiculos/{id}	Remove um veículo
Peça	GET	/pecas	Lista o catálogo de peças
POST	/pecas	Cadastra uma nova peça
Serviço	GET	/servicos	Lista o catálogo de serviços
POST	/servicos	Cadastra um novo serviço
Ordem Serviço	GET	/ordens	Lista todas as OS
GET	/ordens/{id}	Busca OS por ID (inclui valor total)
POST	/ordens	Abre uma nova OS
PUT	/ordens/{id}	Atualiza dados da OS
DELETE	/ordens/{id}	Remove uma OS
Item da OS	POST	/ordens/{id}/itens	Adiciona peça/serviço a uma OS

⚙️ Como Rodar o Projeto
Pré-requisitos
Java 25 (JDK) instalado.

Maven instalado.

Postman ou Insomnia (para testar a API).

1. Clonar o repositório
bash
git clone https://github.com/maicon-castilhos/automechanic-api.git
cd automechanic-api
2. Rodar com H2 (Ambiente de Teste/Desenvolvimento)
O projeto já está configurado para usar o banco em memória H2 por padrão. Isso significa que você não precisa instalar nenhum banco de dados para testar.

bash
mvn spring-boot:run
A API estará disponível em: http://localhost:8080

O console do H2 estará disponível em: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:automechanic_db

User Name: sa

Password: (deixe em branco)

Nota: A classe TestConfig populará o banco automaticamente com dados fictícios (Clientes, Veículos, OS) toda vez que a aplicação iniciar com este perfil.

3. Rodar com PostgreSQL (Ambiente de Produção)
Para rodar com o banco real, você precisa ter o PostgreSQL instalado e um banco chamado automechanic_db criado.

Altere o arquivo src/main/resources/application.properties:

properties
spring.profiles.active=prod
Certifique-se de que o arquivo application-prod.properties está com as suas credenciais (usuário e senha do PostgreSQL).

Rode a aplicação passando o argumento de perfil:

bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
(Ou rode o .jar gerado com java -jar target/automechanic-api.jar --spring.profiles.active=prod).

🧪 Exemplos de JSON (Para testes no Postman)
1. POST /clientes (Criar Cliente)

json
{
    "nome": "João Silva",
    "cpfCnpj": "111.222.333-44",
    "telefone": "99999-9999",
    "email": "joao@email.com"
}
2. POST /veiculos (Criar Veículo)

json
{
    "placa": "JOA-2026",
    "modelo": "Corolla",
    "marca": "Toyota",
    "ano": 2022,
    "cor": "Prata",
    "quilometragem": 10000,
    "cliente": {
        "id": 3
    }
}
3. POST /pecas (Criar Peça)

json
{
    "codigo": "P003",
    "nome": "Pastilha de Freio",
    "fabricante": "Bosch",
    "precoVenda": 80.00
}
4. POST /ordens (Abrir Ordem de Serviço)

json
{
    "numero": "OS-002/2026",
    "dataAbertura": "2026-10-25T10:00:00",
    "quilometragem": 10000,
    "relatoCliente": "Barulho ao frear",
    "status": "ABERTA",
    "desconto": 0.00,
    "cliente": {
        "id": 3
    },
    "veiculo": {
        "id": 3
    }
}
5. POST /ordens/{id}/itens (Adicionar Item à OS)
(Lembre-se de trocar o {id} na URL pelo ID da OS criada acima)

json
{
    "tipo": "PECA",
    "descricao": "Troca de pastilha",
    "quantidade": 1.00,
    "valorUnitario": 80.00
}
🧑‍💻 Autor
Desenvolvido por Maicon Amaral.

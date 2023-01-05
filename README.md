# Pismo - Teste prático

O objetivo desse teste era a criação de uma API simples para cadastro / consulta de contas e transações.

A API foi criada utilizando Java 17, Spring Boot 2.7.7, JPA, H2.
Os testes foram feitos utilizando JUnit, Mockito, DataJpaTest e H2.

A execução da aplicação pode ser feita via Docker, executando os seguintes comandos na pasta raiz:

```
docker-compose build
docker-compose up -d
```

A API será executada na porta 8082 e a documentação via swagger pode ser acessada pela URL:

http://localhost:8082/swagger-ui/index.html

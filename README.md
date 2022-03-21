# Conversor de Moedas
![Travis-CI](https://app.travis-ci.com/rafaelndev/currency-converter.svg?branch=development)

API Rest com a finalizar de converter moedas usando a API [exchangeratesapi.io](https://exchangeratesapi.io/) como provedor da taxa de conversão.

## Tecnologias e Ferramentas Utilizadas
* **Java 11**.
* **Spring Boot**: Framework java open source que facilita no processo de desenvolvimentopor meio de convenção sobre a configuração.
    * **WebFlux**: Modulo do Spring que permite que seja possivel trabalhar com programação reativa e não bloqueante, utilizando o [Project Reactor](https://projectreactor.io/).
    * **WebClient**: Substituito do RestTemplate em ambientes reativos, usado para se comunicar à api externa.
    * **Spring Data R2DBC**: Modulo que integra a lib R2DBC permitindo se conectar com databases relacionais e mantendo o modelo reativo do webflux.
    * **Springdoc**: Modulo que integra a aplicação à especificação de Documentação OpenAPI e ao Swagger-ui (para visualização da documentação)
* **H2**: Banco de dados em memoria integrado à aplicação.
* **Travis CI**: Serviço para integração continua integrado ao github.
* **Heroku**: Plataforma de para hospedagem de projetos na nuvem
* **Testes**
  * **Mockito**: Framework para realizar mocks nos testes unitários.
  * **reactor-test**: Biblioteca para auxiliar nos testes reativos
  * **MockWebServer**: Biblioteca para realizar mocks de chamadas à apis externas.
  * **AssertJ**: Usado para realizar validações de assercões nos testes de forma fluente.

---

## Arquitetura e Organização
Foi usado o modelo arquitetural proposto pelo Spring Boot, focando na separação de responsabilidade de cada camada, evitando que por exemplo, objetos do Resource como responses e requests vazem para o service ou camada de persistência.

### Camada de Apresentação/API (Resource)
Essa camada ficou didida pelos pacotes de _`request`_ (classes de modelo de requisições)e _`response`_ (classes de modelo de respostas).
Já o controller ficou divido entre sua interface _`Resource`_, contendo as assinaturas de método do controller, mas o principal motivo da divisão foi para separar as anotações do OpenApi do Controller, mantendo o mesmo mais légivel.

E por final o Controller que contem a implementação da API Rest, dependente sempre dos services.

### Camada de Regras de Negócios (Service)
No service ficaram as classes de regras de négocio (conversão, e comunicação com repository), e também a classe responsável pela comunicação com a api externa. Contendo também um pacote de _`response`_ com as respostas da api.

### Camada de Persistência (Data)
Essa camada ficou dividida entre _`repository`_, contendo os repositórios do spring data, _`domain`_, com as entidades representando tabelas do banco de dados, e _`dto`_ com as classes de transporte de dados.

### Configuração e Exception
Além das citadas acima, também foi feita uma separação dos pacotes de _`exception`_, contendo as exceções geradas pelo projeto, o ControllerAdvice para tratar essas exceções e suas classes de erro.

No pacote de _`configuration`_ ficam as classes anotadas como configuração do spring.

---

## Execução do Projeto Local
* Necessário editar o arquivo *application.yml* e editar a propriedade _app.config.exchange-rates-api.accessKey_  com a chave de acesso obtida no [exchangeratesapi.io](https://exchangeratesapi.io/)

Então é possivel inicializar a processo pelo comando:
```bash
mvn spring-boot:run
```

Por padrão irá ficar disponível na url:
http://localhost:8080

E a documentação na URL:
http://localhost:8080/docs

---

## Execução usando Travis-CI e Heroku
* Adicionar a chave de acesso do Heroku ao Travis-CI por meio de variável de ambiente `HEROKU_API_KEY`.
* Adicionar a chave de acesso à api externa no Heroku usando a variável de ambiente `EXCHANGE_API_ACCESS_KEY`

Então só executar o CI pelo Travis-CI, e o CD ao Heroku será feito automaticamente caso a configuração esteja correta.

---

## Disponibilidade 
A API está disponível no Heroku

**API**: https://currency-converter-api-rafaeln.herokuapp.com/api/v1/

**Docs**: https://currency-converter-api-rafaeln.herokuapp.com/docs
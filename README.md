# API de Controle de Ponto

O projeto foi feito para auxiliar no controle de ponto dos funcionários de uma organização, de forma que forneça funcionalidades para bater o ponto de funcionários, alocar as horas trabalhadas desses funcionários em projetos específicos e gerar um relatório final contendo todas informações processadas.

> *Objetivo: garantir o controle das horas trabalhadas de um funcionário dentro de uma organização.*



## Clonar o projeto

- `git clone https://github.com/rcdwoods/controle-de-ponto`
- `cd controle-de-ponto`

Agora você poderá executar os comandos abaixo.



## Limpar, compilar, executar testes de unidade e cobertura

- `mvn clean`
  Remove o diretório *target*.

- `mvn compile`
  Compila o projeto e deposita os resultados no diretório *target*.

- `mvn test`
  Executa todos os testes do projeto. Para executar apenas parte dos testes, por exemplo aqueles contidos em uma na classe MomentoServiceTest, utiliza-se o comando`mvn -Dtest=MomentoServiceTest test`.  Para executar um único teste nessa mesma classe, por exemplo, utiliza-se`mvn -Dtest=MomentoServiceTest#nomeDoMetodo test`.

  

## Empacotando o projeto

- `mvn package`
  gera arquivo *exemplo.jar* no diretório *target*. 

  

## Executando a API 

- `mvn spring-boot:run`
  Executa a API na porta padrão (8080). 

  

## Requisições da API

- `



## Documentação

- `mvn javadoc:javadoc`
  produz documentação do projeto depositada em *target/site/apidocs/index.html*. Este comando está configurado para o JDK 9.

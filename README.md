### Utilizando dois banco de dados com Spring e MyBatis.

Primeiro vamos adicionar no arquivo application-dev.yml as configurações dos dois bancos, no exemplo serão dois bancos Oracle.
```
spring:
  application:
    name: doisbancodedados
datasource:
  banco1:
    jdbcUrl: jdbc:oracle:thin:127.0.0.1:1522/banco01
    username: admin
    password: admin
    driver-class-name: oracle.jdbc.driver.OracleDriver
  banco2:
    jdbcUrl: jdbc:oracle:thin:127.0.0.1:1522/banco01
    username: admin
    password: admin
    driver-class-name: oracle.jdbc.driver.OracleDriver

``` 

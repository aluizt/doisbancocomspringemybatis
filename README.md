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

Apos adicionar as configurações dos banco, vamos criar nossas classes de configuração para cada um dos bancos utilizados.

#### Banco1
```
@Configuration
@MapperScan(
        basePackages = "br.com.doisbanco.doisbancodedados.repository.banco1",
        sqlSessionTemplateRef = "banco1SessionTemplate"
)
public class Banco1MyBatisConfiguration {

    @Bean(name = "banco1DataSource")
    @ConfigurationProperties(prefix = "datasource.banco1")
    @Primary
    public DataSource banco1DataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "banco1SessionFactory")
    @Primary
    public SqlSessionFactory banco1SessionFactory(@Qualifier("banco1DataSource") final DataSource banco1DataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(banco1DataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "banco1TransactionManager")
    @Primary
    public DataSourceTransactionManager banco1TransactionManager(@Qualifier("banco1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "banco1SessionTemplate")
    @Primary
    public SqlSessionTemplate banco1SessionTemplate(@Qualifier("banco1SessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}

```

Utilizamos @MapperScan para informar onde estara os repositories que iram acessar este banco, e também definimos o nome da nossa SqlSessionTemplateRef

```

@MapperScan(
        basePackages = "br.com.doisbanco.doisbancodedados.repository.banco1",
        sqlSessionTemplateRef = "banco1SessionTemplate"
)

```  

Criaremos um metodo que ira dos retornar um DataSouce, para isto utilizaremos a notação @ConfigurationProperties com o prefix igual ao datasouce que configuramos no arquivo application-dev.yml

```  

    @Bean(name = "banco1DataSource")
    @ConfigurationProperties(prefix = "datasource.banco1")
    @Primary
    public DataSource banco1DataSource() {
        return new HikariDataSource();
    }
    
 ``` 

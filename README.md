### Utilizando dois banco de dados com Spring e MyBatis.

Neste exemplo irei demostrar como utilizar dois banco de dados com o Spring mais o framework MyBatis.
Seram dois bancos de dados Oracle, contendo as mesmas tabelas apenas para facilitar o entendimento.

Primeiro vamos adicionar no arquivo application-dev.yml as configurações dos dois bancos.
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

Utilizamos @MapperScan para informar onde estara os repositories que irão acessar este banco, e também definimos o nome da nossa SqlSessionTemplateRef

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
 
Precisamos de um SqlSessionFactory, um DataSourceTransactionManage e um SqlSessionTemplate para isso criamos os metodos abaixo informando qual o nome do dataSource através da anotação @Qualifier
 
 ```
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
 ``` 
 
 Agora que temos nossos banco configurados, vamos criar uma inteface para buscar um produto através de um id, lembrando que este é um exemplo simples, apenas com o objetivo de demostrar a utilização de dois bancos de dados através do Spring.
 
 ```  
@Mapper
public interface ProdutoRepository {

    @Select("SELECT * FROM PRODUTO WHERE CODIGO_PRODUTO = #{codigoProduto}")
    ProdutoModel buscarProduto(@Param("codigoProduto") Long codigoProduto);
}
```

Apos iremos criar as interfaces que irão extender a inteface criada acima.

```
@Mapper
public interface Banco1ProdutoRepository extends ProdutoRepository {
}
```  
```
@Mapper
public interface Banco2ProdutoRepository extends ProdutoRepository {
}
``` 

Desta forma quando for necessários salvar do banco 1, apenas utilizamos a inteface Banco1ProdutoRepository, caso queiramos utilizar o banco 2 utilizaremos a interface Banco2ProdutoRepository, como no exemplo abaixo.

```
@SpringBootApplication
@Log4j2
public class DoisbancodedadosApplication {

    Banco1ProdutoRepository banco1ProdutoRepository;
    Banco2ProdutoRepository banco2ProdutoRepository;

    public static void main(String[] args) {
        SpringApplication.run(DoisbancodedadosApplication.class, args);
    }

    @PostConstruct
    public void teste() {
        log.info("Produto do banco 1 : " + banco1ProdutoRepository.buscarProduto(101L));
        log.info("Produto do banco 2 : " + banco2ProdutoRepository.buscarProduto(101L));
    }
}

```


 
 

package br.com.doisbanco.doisbancodedados.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

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

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "br.com.doisbanco.doisbancodedados.repository.banco2",
        sqlSessionTemplateRef = "banco2SessionTemplate"
)
public class Banco2MyBatisConfiguration {

    @Bean(name = "banco2DataSource")
    @ConfigurationProperties(prefix = "datasource.banco2")
    public DataSource banco2DataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "banco2SessionFactory")
    public SqlSessionFactory banco2SessionFactory(@Qualifier("banco2DataSource") final DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "banco2TransactionManager")
    public DataSourceTransactionManager banco2TransactionManager(@Qualifier("banco2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "banco2SessionTemplate")
    public SqlSessionTemplate banco2SessionTemplate(@Qualifier("banco2SessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}

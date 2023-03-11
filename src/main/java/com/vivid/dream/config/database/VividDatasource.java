package com.vivid.dream.config.database;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan("com.vivid.dream.mapper")
@MapperScan(value = "com.vivid.dream.mapper", sqlSessionFactoryRef = "vividSqlSessionFactory")
public class VividDatasource {
    @Bean(name = "vividDataSource")
    @ConfigurationProperties(prefix = "spring.database")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "vividSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("vividDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        factoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "ibAuthSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Autowired @Qualifier("vividSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);

    }
}
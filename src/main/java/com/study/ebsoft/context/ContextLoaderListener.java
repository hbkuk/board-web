package com.study.ebsoft.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 서블릿 컨텍스트 초기화 및 소멸 시에 호출되는 리스너 클래스입니다
 * 데이터베이스 연결 및 초기화 작업을 수행합니다
 */
@Slf4j
@WebListener
public class ContextLoaderListener implements ServletContextListener {

    /**
     * 서블릿 컨텍스트 초기화 시 호출되는 메서드입니다
     * 데이터베이스 연결 및 초기화 작업을 수행합니다
     *
     * @param sce 서블릿 컨텍스트 이벤트 객체
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3316/ebsoft");
        dataSource.setUsername("ebsoft");
        dataSource.setPassword("123456");

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("initdb.sql"));

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            populator.populate(dataSource.getConnection());
        } catch (Exception e) {
            log.error("An error occurred while executing database scripts.", e);
        }

        log.info("Completed Load ServletContext!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}

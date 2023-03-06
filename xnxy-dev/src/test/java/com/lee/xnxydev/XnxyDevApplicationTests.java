package com.lee.xnxydev;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class XnxyDevApplicationTests {

    @Resource
    private DataSource dataSource;
    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

}

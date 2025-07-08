// src/main/java/com/my/pos/util/JdbcUtil.java
package com.my.pos.util;

import java.io.InputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream is = JdbcUtil.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            url      = prop.getProperty("url");
            user     = prop.getProperty("user");
            password = prop.getProperty("password");
        } catch (Exception e) {
            throw new RuntimeException("db.properties 로드 실패", e);
        }
    }

    /**
     * 기존 기능 유지하면서 SQLException만 던지도록 변경
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("커넥션 획득 실패", e);
        }
    }

}

package com.bc.sr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MyDB {

    protected Connection conn;

    public MyDB() throws SQLException {
        String JDBC_DRIVER;
        String DB_URL;
        String USERNAME;
        String PASS;
        ResourceBundle resource = ResourceBundle.getBundle("DBproperties");
        JDBC_DRIVER = resource.getString("JDBC_DRIVER");
        DB_URL = resource.getString("DB_URL");
        USERNAME = resource.getString("USERNAME");
        PASS = resource.getString("PASS");
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
            System.out.println("连接数据库...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("连接数据库成功！");
    }

    public void EstablishTable(String tablename) {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS " + tablename);
            String sqlCreateTabel = "CREATE TABLE " + tablename + " (" +
                    "id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "keyword varchar(200) NOT NULL," +
                    "URL varchar(2000));";
            stmt.execute(sqlCreateTabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitInsert(String sql) throws SQLException {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

}

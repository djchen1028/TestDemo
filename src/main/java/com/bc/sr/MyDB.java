package com.bc.sr;

import java.sql.*;
import java.util.*;

public class MyDB {

    protected Connection conn;

    public MyDB() {
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
            stmt.execute("DROP TABLE IF EXISTS `" + tablename+"`");
            String sqlCreateTabel = "CREATE TABLE `" + tablename + "` (" +
                    "id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "keyword varchar(200) NOT NULL," +
                    "URL varchar(2000)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
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

    public  List<Map<String,Object>> getContent(String tablename) {
        Map<String, Object> map = new HashMap<String, Object>();
        ResultSet rs = null;
        List<Map<String,Object>> links =new ArrayList<Map<String,Object>>();
        List<String[]> dataLists = new ArrayList<String[]>();
        int results_size=0;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select count(*) as count from `"+tablename+"`");
            while(rs.next()) {
                results_size = rs.getInt("count");
                map.put("results_size", results_size);
                links.add(map);
            }
            rs = stmt.executeQuery("select * from `" + tablename+"`" );
            while (rs.next()) {
                map = new HashMap<String,Object>();
                String[] data=new String[3];
                data[0]=rs.getString("id");
                data[1]=rs.getString("keyword");
                data[2]=rs.getString("URL");
                map.put("keyword",data[1]);
                map.put("URL",data[2]);
                dataLists.add(data);
                links.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        CSVUtils.write(tablename,dataLists);

        return links;
    }
}

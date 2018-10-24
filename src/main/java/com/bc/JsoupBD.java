package com.bc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static java.lang.System.in;
import static java.lang.System.out;

public class JsoupBD {
    public static void main(String[] args) throws Exception{

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String keyword = br.readLine();
        String url ;
        String sql="";
        String kw = URLEncoder.encode(keyword,"utf-8");
        for(int i=0;i<=230;i=i+10){
            url = "https://www.baidu.com/s?wd="+kw+"&pn="+i;
            out.println(url);
            sql = sql+getPageHtmltoInsertData(url);
        }
        sql =  sql.substring(0, sql.length() - 1) + ";";
        sql = "insert into " + keyword + "(keyword,URL) values" + sql;

        MyDB myDB = new MyDB();
        myDB.EstablishTable(keyword);
        myDB.commitInsert(sql);

    }

    public static String getPageHtmltoInsertData(String Url) throws IOException {
        String sql="";
        String text;
        String url="";
        Document doc = Jsoup.connect(Url).get();
        Elements h3s= doc.getElementsByTag("h3");
        for(Element h3 : h3s){
            out.println(h3.text());
            text=h3.text();
            text = text.replaceAll("'","\\\\'");
            Elements anchors = h3.getElementsByTag("a");
            for(Element anchor : anchors){
//                out.println(anchor.attr("href"));
                url=anchor.attr("href");
                break;
            }
            sql=sql+ "('" + text + "','" + url + "'),";
        }
        return sql;
    }
}

class MyDB{

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
        PASS =resource.getString("PASS");
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USERNAME,PASS);
            System.out.println("连接数据库...");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("连接数据库成功！");
    }

    public void EstablishTable(String tablename){
        Statement stmt ;
        try {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS "+tablename);
            String sqlCreateTabel = "CREATE TABLE " + tablename + " (" +
                    "id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "keyword varchar(200) NOT NULL," +
                    "URL varchar(2000));";
            stmt.execute(sqlCreateTabel);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void commitInsert(String sql) throws SQLException {
        Statement stmt ;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            conn.close();
        }
    }

}

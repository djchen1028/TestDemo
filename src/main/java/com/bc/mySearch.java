package com.bc;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.*;

public class mySearch {
    private static String keyWords;
    private static boolean isEndPage;
    private static Connection conn;
    private static String insertSql;

    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        HtmlPage baiduPage = null;
        isEndPage = false;
        out.println("请输入查询内容:");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        keyWords = br.readLine();
        //baiduPage = search(keyWords);
        //System.out.println(baiduPage.asText());
        search(keyWords);
        //nextBaiduPage(firstBaiduPage);
//        System.out.println(firstBaiduPage.asText());
    }

    private static void search(String kw) throws IOException, SQLException, InterruptedException {
        String tagName;
        DomElement domElement;
        final MySQL mysql = new MySQL();
        insertSql="";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);
        HtmlPage firstPage = webClient.getPage("https://www.baidu.com/s?wd="+keyWords);
//        out.println(firstPage.asText());
        conn = mysql.EstablshDB(keyWords);
        insertSql = insertSql+mysql.getInsertDataSql(firstPage);
        mysql.commitInsert(conn,insertSql,keyWords);
//        HtmlInput input = (HtmlInput) htmlpage.getElementById("kw");
//        HtmlInput search_btn = (HtmlInput) htmlpage.getElementById("su");
//        input.setValueAttribute(kw);
//        HtmlPage firstPage = search_btn.click();

        String template =null;
        Iterator<DomElement> DomIterator = firstPage.getElementById("page").getChildElements().iterator();

        while(DomIterator.hasNext()){
            domElement = DomIterator.next();
            tagName = domElement.getTagName();
            if(tagName.equals("a")){
                template= "https://www.baidu.com"+domElement.getAttribute("href");
                break;
            }
        }

        webClient.close();

        //String template = "https://www.baidu.com/s?wd="+keyWords;
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for(int i = 10;i<=30;i=i+10){
            final String tempurl = template.replaceAll("pn=10","pn="+i);
            out.println(tempurl);
            Thread.sleep(2000);
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
//                        out.println(Thread.currentThread().getName());
                        insertSql = insertSql+getNextPage(tempurl);
                        mysql.commitInsert(conn,insertSql,keyWords);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

//            getNextPage(tempurl);
        }
        cachedThreadPool.shutdown();
        while (true) {
            if(cachedThreadPool.isTerminated()){

                conn.close();
                break;
            }
            Thread.sleep(1000);
        }
//        webClient.waitForBackgroundJavaScript(20000);
//        out.println(nextPage.asText());


    }

    private static String getNextPage(String url) throws IOException, SQLException {
        HtmlPage nextPage;
        MySQL mysql = new MySQL();
        conn = mysql.ConnectDB();
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
//        webClient.waitForBackgroundJavaScript(2000);
        nextPage = webClient.getPage(url);
        //out.println(nextPage.asText());
        return (mysql.getInsertDataSql(nextPage));
    }
}

class MySQL{
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL= "jdbc:mysql://localhost:3306/demo_project?useSSL=false&serverTimezone=UTC";

    private static final String USERNAME = "root";
    private static final String PASS="dj_chen1028";
    private static String tablename;

    public Connection ConnectDB() throws SQLException {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USERNAME,PASS);
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public Connection EstablshDB(String kw) throws SQLException {
        Connection conn = null;

        Statement stmt ;
        tablename = kw;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USERNAME,PASS);
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
        return conn;
    }


        public String getInsertDataSql (HtmlPage htmlPage) {

            String sqlinsert = "";
            DomNodeList<HtmlElement> domElementList = htmlPage.getElementById("content_left").getElementsByTagName("h3");
            Iterator<HtmlElement> domElementIterator = domElementList.iterator();
            while (domElementIterator.hasNext()) {
                DomElement h3Element = domElementIterator.next();
//                if (h3Element.getLastElementChild().getAttribute("data-click") != null) {
                    String text = h3Element.getLastElementChild().asText();
                    if (text.contains("'")) {
                        text = text.replaceAll("'", "\\\\\'");
                    }
                    String url = h3Element.getLastElementChild().getAttribute("href");
                    sqlinsert = sqlinsert + "('" + text + "','" + url + "'),";
                    out.println(text);
//                }
            }
//            sqlinsert = sqlinsert.substring(0, sqlinsert.length() - 1) + ";";
            return sqlinsert;

//            sqlinsert = "insert into " + tablename + "(keyword,URL) values" + insertSql;

        }

        public void commitInsert(Connection conn,String sql,String keyWords) throws SQLException {
            Statement stmt ;
            String insertSql = sql.substring(0, sql.length() - 1) + ";";
            insertSql = "insert into " + keyWords + "(keyword,URL) values" + insertSql;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
                stmt = conn.createStatement();
                stmt.execute(insertSql);
            }catch(Exception e){
                e.printStackTrace();
                conn.close();
            }
        }
}
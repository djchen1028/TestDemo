package com.bc.sr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import static java.lang.System.in;
import static java.lang.System.out;

public class JsoupBD {
    public static boolean isEndPage = false;

    public static void main(String[] args) throws Exception {
        out.print("请输入搜索内容：");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String keyword = br.readLine();
        String url;
        String sql = "";
        String kw = URLEncoder.encode(keyword, "utf-8");
        for (int i = 0; i<=30; i = i + 10) {
            url = "https://www.baidu.com/s?wd=" + kw + "&pn=" + i;
            out.println(url);
            sql = sql + getPageHtmltoInsertData(url);
        }
        sql = sql.substring(0, sql.length() - 1) + ";";
        sql = "insert into " + keyword + "(keyword,URL) values" + sql;

        MyDB myDB = new MyDB();
        myDB.EstablishTable(keyword);
        myDB.commitInsert(sql);

    }

    public static String getPageHtmltoInsertData(String Url) throws IOException {
        String sql = "";
        String text = null;
        String url = "";
        Document doc = Jsoup.connect(Url).get();
        Element div = doc.getElementById("page");
        if(!div.text().contains("下一页")||div==null){
            isEndPage=true;
        }
        Elements h3s = doc.getElementsByTag("h3");
        for (Element h3 : h3s) {
            /**
             *
             *爬取百度搜索带广告的记录集
            out.println(h3.text());
            text = h3.text();
            text = text.replaceAll("'", "\\\\'");
            Elements anchors = h3.getElementsByTag("a");
            */
            /**
             * 爬取百度搜索未带广告的记录集
             */
            Elements anchors = h3.select("a[data-click]");
            for (Element anchor : anchors) {
//                out.println(anchor.attr("href"));
                url = anchor.attr("href");
                text = anchor.text();
                out.println(text);
                text = text.replaceAll("'", "\\\\'");
                sql = sql + "('" + text + "','" + url + "'),";
                break;
            }
//            sql = sql + "('" + text + "','" + url + "'),";
        }

        return sql;
    }
}


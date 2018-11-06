package com.bc.controller;

import com.bc.sr.JsoupBD;
import com.bc.sr.MyDB;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;

@Controller
@RequestMapping("/index")
public class IndexController {
    private static String sql;

    @RequestMapping("test")
    public String test(HttpServletRequest httpServletRequest, Map<String, Object> map) throws Exception{
        String s = httpServletRequest.getParameter("kw");
        JsoupBD.isEndPage = false;//initialize parameter
        sql="";//initialize parameter
        if (s == null) {
            return "index";
        }
//        String tablename =s.replace(' ','_');
        final String tablename = s;
//        String url;
        String kw = URLEncoder.encode(s, "utf-8");
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (int i = 0; JsoupBD.isEndPage == false; i = i + 10) {
            final int index = i/10;
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            final String url = "https://www.baidu.com/s?wd=" + kw + "&pn=" + i;
            out.println(url);
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        out.println(Thread.currentThread().getName());
                        sql = sql + JsoupBD.getPageHtmltoInsertData(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        cachedThreadPool.shutdown();
        while(true){
            if(cachedThreadPool.isTerminated()){
                sql = sql.substring(0, sql.length() - 1) + ";";
                sql = "insert into `" + tablename + "`(keyword,URL) values" + sql;
                MyDB myDB = new MyDB();
                myDB.EstablishTable(tablename);
                myDB.commitInsert(sql);
                MyDB getDataDB = new MyDB();
                List<Map<String, Object>> links = new ArrayList<Map<String, Object>>();
                links = getDataDB.getContent(tablename);
                int count = (int) links.get(0).get("results_size");
                links.remove(0);
//        links.subList(0,20);
                map.put("str", s);
                map.put("tablename", tablename);
                map.put("count", count);
                if (links.size() > 20) {
                    map.put("links", links.subList(0, 20));
                } else {
                    map.put("links", links);
                }
                return "index2";
            }
        }
    }

    @RequestMapping("")
    public String index() {
        return "index";
    }
}

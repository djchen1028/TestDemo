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

import static java.lang.System.out;

@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("test")
    public String test(HttpServletRequest httpServletRequest, Map<String, Object> map) throws IOException, SQLException {
        String s = httpServletRequest.getParameter("kw");
        JsoupBD.isEndPage=false;//初始化参数
        if(s==null){
            return "index";
        }
        String tablename =s.replace(' ','_');
        String url;
        String sql = "";
        String kw = URLEncoder.encode(s, "utf-8");
        for (int i = 0 ; JsoupBD.isEndPage == false; i = i + 10) {
            url = "https://www.baidu.com/s?wd=" + kw + "&pn=" + i;
            out.println(url);
            sql = sql + JsoupBD.getPageHtmltoInsertData(url);
        }
        sql = sql.substring(0, sql.length() - 1) + ";";
        sql = "insert into " + tablename + "(keyword,URL) values" + sql;

        MyDB myDB = new MyDB();
        myDB.EstablishTable(tablename);
        myDB.commitInsert(sql);
        MyDB getDataDB = new MyDB();
        List<Map<String,Object>> links = new ArrayList<Map<String,Object>>();
        links= getDataDB.getContent(tablename);
        int count = (int) links.get(0).get("results_size");
        links.remove(0);
//        links.subList(0,20);
        map.put("str",s);
        map.put("tablename",tablename);
        map.put("count",count);
        if(links.size()>20) {
            map.put("links", links.subList(0, 20));
        }
        else {
            map.put("links",links);
        }
        return "index2";
    }

    @RequestMapping("")
    public String index() {
        return "index";
    }
}

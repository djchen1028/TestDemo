package com.bc.controller;

import com.bc.sr.JsoupBD;
import com.bc.sr.MyDB;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

import static java.lang.System.out;

@Controller
@RequestMapping("/index")
public class IndexController {
    @RequestMapping("test")
    public String test(HttpServletRequest httpServletRequest, Model model) throws IOException, SQLException {
//        String s = "this is from Server";
        String s = httpServletRequest.getParameter("kw");
        model.addAttribute("str", s);

        String url;
        String sql = "";
        String kw = URLEncoder.encode(s, "utf-8");
        for (int i = 0; i <= 60; i = i + 10) {
            url = "https://www.baidu.com/s?wd=" + kw + "&pn=" + i;
            out.println(url);
            sql = sql + JsoupBD.getPageHtmltoInsertData(url);
        }
        sql = sql.substring(0, sql.length() - 1) + ";";
        sql = "insert into " + s + "(keyword,URL) values" + sql;

        MyDB myDB = new MyDB();
        myDB.EstablishTable(s);
        myDB.commitInsert(sql);

        return "index2";
    }

    @RequestMapping("")
    public String index() {
        return "index";
    }
}

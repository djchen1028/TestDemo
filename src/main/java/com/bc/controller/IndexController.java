package com.bc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {
    @RequestMapping("test")
    public String test(Model model){
        String s = "this is from Server";
        model.addAttribute("str",s);
        return "index2";
    }

    @RequestMapping("")
    public String index(){
        return "index";
    }
}

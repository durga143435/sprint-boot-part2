package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/home")
    public String getHome(Model model){
        model.addAttribute("name", "This is my name");
        return "homepage";
    }

   /* @RequestMapping("/greetings")
    public Message getGreeting(){
        return new Message("Hey hello, Good evening");
    }*/
}

package com.pan1024.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}

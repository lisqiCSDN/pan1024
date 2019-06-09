package com.pan1024.controller;

import com.pan1024.service.AdminService;
import com.pan1024.vo.ResultVoidVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class LoginController {

    @Autowired private AdminService adminService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public ResultVoidVO login(@RequestParam("name") String name,
                              @RequestParam("password") String password, HttpServletRequest request){
        return adminService.login(name,password,request);
    }
}

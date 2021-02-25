package com.pan1024.controller;

import com.pan1024.entity.Admin;
import com.pan1024.service.AdminService;
import com.pan1024.vo.ResultOneVO;
import com.pan1024.vo.ResultPageVO;
import com.pan1024.vo.ResultVoidVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AdminService adminService;
//    @Autowired private AdminService adminService;

    @GetMapping("/find/all/view")
    public String findAll() {
        return "admin/admin-list";
    }

    @ResponseBody
    @GetMapping("/find/all")
    public ResultPageVO<Admin> findAll(@RequestParam(required = false,defaultValue = "1")Integer page,
                                       @RequestParam(required = false,defaultValue = "10")Integer pageSize) {
        return adminService.findAll(page,pageSize);
    }

    @ResponseBody
    @PostMapping("/update/password")
    public ResultVoidVO updatePassword(@RequestParam Long id, @RequestParam String newPassword){
        return adminService.updatePassword(id, newPassword);
    }

    @ResponseBody
    @PostMapping("/add")
    public ResultOneVO<Admin> add(String name, String password) {
        return adminService.add(name,password);
    }

    @ResponseBody
    @PostMapping("/del")
    public ResultVoidVO delAdmin(@RequestParam Long id) {
        return adminService.delAdmin(id);
    }
}

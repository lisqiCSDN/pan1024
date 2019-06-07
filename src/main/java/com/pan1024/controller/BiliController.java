package com.pan1024.controller;

import com.pan1024.service.BiliInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: BiliController
 * @Date: 2019/6/6
 * @describe:
 */
@Slf4j
@Controller
@RequestMapping("bili")
public class BiliController {

    @Autowired
    private BiliInfoService biliSpiderService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/info/view")
    public Object infoView() {
        return "admin/bilibili/bilibili-info";
    }

    @ResponseBody
    @GetMapping("/info/list")
    public Object list(@RequestParam(required = false,defaultValue = "1")Integer page,
                       @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                       String search) {
        return biliSpiderService.list(page,pageSize,search);
    }

    @ResponseBody
    @PostMapping("/start")
    public void start(@RequestParam(defaultValue = "1")Integer thread,@RequestParam(defaultValue = "100") Integer count){
        biliSpiderService.start(thread,count);
    }

    @ResponseBody
    @GetMapping("/stop")
    public void stop(){
        biliSpiderService.stop();
    }

}

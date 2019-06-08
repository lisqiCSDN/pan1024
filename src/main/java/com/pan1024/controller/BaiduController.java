package com.pan1024.controller;

import com.pan1024.service.BaiduService;
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
@RequestMapping("baidu")
public class BaiduController {

    @Autowired
    private BaiduService baiduService;

    @GetMapping("/info/view")
    public Object infoView() {
        return "baidu/baidu-info";
    }

    @ResponseBody
    @GetMapping("/info/list")
    public Object list(@RequestParam(required = false,defaultValue = "1")Integer page,
                       @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                       String search) {
        return baiduService.list(page,pageSize,search);
    }

    @GetMapping("/processor/view")
    public Object processorView() {
        return "baidu/baidu-processor";
    }

    @ResponseBody
    @PostMapping("/info/start")
    public void infoStart(@RequestParam(defaultValue = "1")Integer thread,
                          @RequestParam(defaultValue = "100")Integer count){
        baiduService.baiduStart(count);
    }

    @ResponseBody
    @GetMapping("/info/stop")
    public void stop(){
        baiduService.baiduStop();
    }
}

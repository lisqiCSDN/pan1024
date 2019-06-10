package com.pan1024.controller;

import com.pan1024.service.BaiduService;
import com.pan1024.vo.ResultVoidVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String infoView() {
        return "baidu/baidu-info";
    }

    @ResponseBody
    @GetMapping("/info/list")
    public Object list(@RequestParam(required = false,defaultValue = "1")Integer page,
                       @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                       String search,Long uk) {
        return baiduService.list(page,pageSize,search,uk);
    }

    @GetMapping("/processor/view")
    public String processorView() {
        return "baidu/baidu-processor";
    }

    @ResponseBody
    @PostMapping("/info/start")
    public void infoStart(@RequestParam(defaultValue = "1")Integer thread,
                          @RequestParam(defaultValue = "100")Integer count){
        baiduService.baiduStart(count);
    }

    @ResponseBody
    @PostMapping("/again/start")
    public ResultVoidVO againStart(@RequestParam List<Long> uks){
        return baiduService.againStart(uks);
    }

    @ResponseBody
    @GetMapping("/info/stop")
    public void stop(){
        baiduService.baiduStop();
    }

    @ResponseBody
    @GetMapping("/find/vacancy")
    public List<Long> findVacancy(@RequestParam(defaultValue = "1")Long begin,
                                  @RequestParam(defaultValue = "100")Long count){
        return baiduService.findVacancy(begin,count);
    }
}

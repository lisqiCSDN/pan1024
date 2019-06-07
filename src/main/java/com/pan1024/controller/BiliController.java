package com.pan1024.controller;

import com.pan1024.service.BiliFollowerService;
import com.pan1024.service.BiliInfoService;
import com.pan1024.service.BiliPlayService;
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
    @Autowired
    private BiliFollowerService followerService;
    @Autowired
    private BiliPlayService playService;

    @GetMapping("/info/view")
    public Object infoView() {
        return "bilibili/bilibili-info";
    }

    @ResponseBody
    @GetMapping("/info/list")
    public Object list(@RequestParam(required = false,defaultValue = "1")Integer page,
                       @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                       String search) {
        return biliSpiderService.list(page,pageSize,search);
    }

    @GetMapping("/processor/view")
    public Object processorView() {
        return "bilibili/bilibili-processor";
    }

    @ResponseBody
    @PostMapping("/info/start")
    public void infoStart(@RequestParam(defaultValue = "1")Integer thread,
                          @RequestParam(defaultValue = "100")Integer count){
        biliSpiderService.infoStart(thread,count);
    }

    @ResponseBody
    @GetMapping("/info/stop")
    public void stop(){
        biliSpiderService.stop();
    }

    /**
     * 获取用户关注，粉丝数
     */
    @ResponseBody
    @PostMapping("/follower/start")
    public void followerStart(@RequestParam(defaultValue = "1")Integer thread,
                              @RequestParam(defaultValue = "100")Integer count){
        followerService.followerStart(count);
    }

    @ResponseBody
    @PostMapping("/follower/stop")
    public void followerStop(){
        followerService.followerStop();
    }

    /**
     * 获取用户播放数
     */
    @ResponseBody
    @PostMapping("/play/start")
    public void playStart(@RequestParam(defaultValue = "1")Integer thread,
                              @RequestParam(defaultValue = "100")Integer count){
        playService.playStart(count);
    }

    @ResponseBody
    @PostMapping("/play/stop")
    public void playStop(){
        playService.playStop();
    }
}

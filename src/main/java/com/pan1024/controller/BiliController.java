//package com.pan1024.controller;
//
//import com.pan1024.service.SpiderService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @ClassName: BiliController
// * @Date: 2019/6/6
// * @describe:
// */
//@Slf4j
//@RestController
//public class BiliController {
//
//    @Autowired
//    private SpiderService spiderService;
//
//
//    @GetMapping("/start/{count}")
//    public void start(@PathVariable Integer count){
//        if (count == null) count = 100;
//        spiderService.start(count);
//    }
//
//    @GetMapping("/stop")
//    public void stop(){
//        spiderService.stop();
//    }
//
//}

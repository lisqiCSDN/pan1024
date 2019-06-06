//package com.pan1024.service;
//
//import com.pan1024.download.BiliSpiderPageProcessor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import us.codecraft.webmagic.Request;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.downloader.HttpClientDownloader;
//import us.codecraft.webmagic.utils.HttpConstant;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @ClassName: SpiderService
// * @Date: 2019/6/6
// * @describe:
// */
//@Component
//@Slf4j
//public class SpiderService {
//    @Autowired
//    private BiliPipeline biliPipeline;
//    @Autowired
//    private BiliSpiderPageProcessor biliSpider2;
//    @Autowired
//    private BiliUserService biliUserService;
//    @Autowired
//    private ProxyIpService proxyIpService;
//
//    private Spider spider;
//    @PostConstruct
//    private void init(){
//        spider = Spider.create(biliSpider2)
//                .addPipeline(biliPipeline)
//                .thread(10);
//    }
//
//    public void start(Integer count){
//        biliPipeline.clean();
//        Integer maxMid = biliUserService.getMaxMid();
//        System.out.println(maxMid);
//        if(maxMid == null){
//            maxMid = 0;
//        }
//        for (int i = maxMid; i < maxMid+count; i++) {
//            Request request = new Request("https://space.bilibili.com/ajax/member/GetInfo");
//            request.setMethod(HttpConstant.Method.POST);
//            Map<String, Object> map = new HashMap<>();
//            map.put("mid", i+1);
//            request.setRequestBody(HttpRequestBody.form(map,"utf-8"));
//            request.addHeader("Referer", "https://space.bilibili.com");
//            spider.addRequest(request);
//        }
//        HttpClientDownloader downloader = new HttpClientDownloader();
//        List<ProxyIp> list = proxyIpService.list(new QueryWrapper<ProxyIp>()
//                .orderByAsc("connect_speed")
//                .last("limit 1000"));
//        List<Proxy> proxyList = new ArrayList<>();
//        for (ProxyIp proxyIp : list) {
//            proxyList.add(new Proxy(proxyIp.getIp(), proxyIp.getPort()));
//        }
//        downloader.setProxyProvider(SimpleProxyProvider.from(proxyList.toArray(new Proxy[0])));
//        spider.setDownloader(downloader);
//        spider.start();
//    }
//
//    public void stop(){
//        spider.stop();
//    }
//
//    public Spider getSpider() {
//        return spider;
//    }
//}
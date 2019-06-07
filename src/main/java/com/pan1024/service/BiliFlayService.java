package com.pan1024.service;

import com.pan1024.constant.BiliUrlConstant;
import com.pan1024.pipeline.BiliPipeline;
import com.pan1024.processor.BiliFlayPageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import javax.annotation.PostConstruct;

/**
 * @ClassName: SpiderService
 * @Date: 2019/6/6
 * @describe: 手动控制爬虫
 */
@Slf4j
@Component
public class BiliFlayService {
    @Autowired
    private BiliPipeline biliPipeline;
    @Autowired
    private BiliFlayPageProcessor biliFlayPageProcessor;

    private Spider spider;
    @PostConstruct
    private void init(){
        spider = Spider.create(biliFlayPageProcessor)
                .addPipeline(biliPipeline);
    }

    @Async
    public void start(Integer thread,Integer count,Long maxMid){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (long i = maxMid; i < maxMid+count+1; i++) {
            String url = BiliUrlConstant.FLAY_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
            spider.addUrl(url);
        }
        spider.thread(thread).start();
    }

    public void stop(){
        spider.stop();
    }
}
package com.pan1024.scheduler;

import com.pan1024.constant.BaiduUrlConstant;
import com.pan1024.pipeline.BaiduPipeline;
import com.pan1024.processor.BaiduyunPageProcessor;
import com.pan1024.repository.BaiduUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * @author: Celine Q
 * @create: 2018-09-17 19:59
 **/
@Slf4j
@Component
public class BaiduInfoScheduler {

    @Autowired
    private BaiduPipeline pipeline;
    @Autowired
    private BaiduyunPageProcessor baiduyunPageProcessor;
    @Autowired
    private BaiduUserRepository repository;

    private static final long counts=4;
    @Scheduled(cron ="0/2 * * * * *")
    public void infoScheduled() {
        log.info("----- 开始执行定时任务 -----");
        try {
            Long maxMid = repository.maxMid();
            maxMid=maxMid==null?1:maxMid+1;
            Spider spider = Spider.create(baiduyunPageProcessor).addPipeline(pipeline);
            for (long i=maxMid;i<maxMid+counts;i++){
                String url = BaiduUrlConstant.INFO_URL.replace(BaiduUrlConstant.REPLACE_NAME, String.valueOf(i));
                spider.addUrl(url);
            }
            spider.thread(1).run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

}



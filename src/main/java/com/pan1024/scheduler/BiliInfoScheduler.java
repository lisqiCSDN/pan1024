package com.pan1024.scheduler;

import com.pan1024.constant.BiliUrlConstant;
import com.pan1024.pipeline.BiliPipeline;
import com.pan1024.processor.BiliFollowerPageProcessor;
import com.pan1024.processor.BiliInfoPageProcessor;
import com.pan1024.processor.BiliPlayPageProcessor;
import com.pan1024.repository.BiliUserRepository;
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
public class BiliInfoScheduler {

    @Autowired
    private BiliPipeline pipeline;
    @Autowired
    private BiliInfoPageProcessor infoPageProcessor;
    @Autowired
    private BiliFollowerPageProcessor followerPageProcessor;
    @Autowired
    private BiliPlayPageProcessor playPageProcessor;
    @Autowired
    private BiliUserRepository repository;

    private static final long counts=1300;
    @Scheduled(cron ="0 0 * * * *")
    public void infoScheduled() {
        log.info("----- 开始执行定时任务 -----");
        try {
            Long maxMid = repository.maxMid()+1;
            Spider spider = Spider.create(infoPageProcessor).addPipeline(pipeline);
            for (long i=maxMid;i<maxMid+counts;i++){
                String url = BiliUrlConstant.INFO_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
                spider.addUrl(url);
            }
            spider.thread(4).run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron ="0 20 * * * *")
    public void followerScheduled() {
        log.info("----- 开始执行定时任务 -----");
        try {
            Long maxMid = repository.fansMinMid();
            if (maxMid==null){
                return;
            }
            Spider spider = Spider.create(followerPageProcessor).addPipeline(pipeline);
            for (long i=maxMid;i<maxMid+counts;i++){
                String url = BiliUrlConstant.FOLLOWER_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
                spider.addUrl(url);
            }
            spider.thread(4).run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron ="0 40 * * * *")
    public void playScheduled() {
        log.info("----- 开始执行定时任务 -----");
        try {
            Long maxMid = repository.playMinMid();
            if (maxMid==null){
                return;
            }
            Spider spider = Spider.create(playPageProcessor).addPipeline(pipeline);
            for (long i=maxMid;i<maxMid+counts;i++){
                String url = BiliUrlConstant.PLAY_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
                spider.addUrl(url);
            }
            spider.thread(4).run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

}



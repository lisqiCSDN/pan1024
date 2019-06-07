package com.pan1024.scheduler;

import com.pan1024.pipeline.BiliPipeline;
import com.pan1024.processor.BiliFollowerPageProcessor;
import com.pan1024.processor.BiliInfoPageProcessor;
import com.pan1024.repository.BiliUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    static Long maxMid;
    @Autowired
    private BiliUserRepository repository;

    @Scheduled(cron ="*/5 * * * * ?")
    public void infoScheduled() {
        log.info("----- 开始执行定时任务 -----");
        try {
            if (maxMid==null){
                maxMid = repository.MaxMid();
            }
            maxMid=maxMid==null||maxMid==0?1:maxMid+1;
            for (long i=maxMid;i<maxMid+5;i++){
                long finalI = i;
                new Thread(){
                    @Override
                    public void run() {
                        synchronized(this){
                            maxMid= finalI;
                            System.out.println(Thread.currentThread().getName()+"---------"+maxMid);
                        }
                    }
                }.start();
            }

//            Spider spider = Spider.create(infoPageProcessor).addPipeline(pipeline);
//            for (long i=maxMid;i<maxMid+5;i++){
//                maxMid=i;
//                String url = BiliUrlConstant.INFO_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
//                spider.addUrl(url);
//            }
//            spider.thread(4).run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron ="*/5 * * * * ?")
    public void followerScheduled() {
//        log.info("----- 开始执行定时任务 -----");
//        try {
//            Long maxMid = repository.MaxMid();
//            maxMid=maxMid==null||maxMid==0?1:maxMid+1;
//            Spider spider = Spider.create(followerPageProcessor).addPipeline(pipeline);
//            for (long i=maxMid;i<maxMid+5;i++){
//                String url = BiliUrlConstant.INFO_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
//                spider.addUrl(url);
//            }
//            spider.thread(4).run();
//        }catch (Exception e){
//            log.error(e.getMessage());
//        }
    }

}



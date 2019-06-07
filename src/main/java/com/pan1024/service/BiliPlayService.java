package com.pan1024.service;

import com.pan1024.constant.BiliUrlConstant;
import com.pan1024.pipeline.BiliPipeline;
import com.pan1024.processor.BiliPlayPageProcessor;
import com.pan1024.repository.BiliUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import javax.annotation.PostConstruct;

/**
 * @ClassName: SpiderService
 * @Date: 2019/6/6
 * @describe: bili获取用户播放数
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BiliPlayService {
    @Autowired
    private BiliPipeline biliPipeline;
    @Autowired
    private BiliPlayPageProcessor biliPlayPageProcessor;
    @Autowired
    private BiliUserRepository biliUserRepository;

    private Spider spider;
    @PostConstruct
    private void init(){
        spider = Spider.create(biliPlayPageProcessor)
                .addPipeline(biliPipeline);
    }

    @Async
    public void playStart(Integer count){
        try {
            Long maxMid = biliUserRepository.playMinMid();
            if (maxMid==null){
                return;
            }
            for (long i=maxMid;i<maxMid+count;i++){
                String url = BiliUrlConstant.PLAY_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
                spider.addUrl(url);
            }
            spider.thread(1).run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void playStop(){
        spider.stop();
    }
}
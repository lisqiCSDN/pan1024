package com.pan1024.service;

import com.pan1024.constant.BiliUrlConstant;
import com.pan1024.entity.BiliUser;
import com.pan1024.pipeline.BiliPipeline;
import com.pan1024.processor.BiliInfoPageProcessor;
import com.pan1024.repository.BiliUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @ClassName: SpiderService
 * @Date: 2019/6/6
 * @describe: 手动控制爬虫
 */
@Slf4j
@Component
public class BiliInfoService {
    @Autowired
    private BiliPipeline biliPipeline;
    @Autowired
    private BiliInfoPageProcessor biliSpiderPageProcessor;
    @Autowired
    private BiliUserRepository biliUserRepository;

    private Spider spider;
    @PostConstruct
    private void init(){
        spider = Spider.create(biliSpiderPageProcessor)
                .addPipeline(biliPipeline)
                .thread(10);
    }

    public void start(Integer count){
        Long maxMid = biliUserRepository.MaxMid();
        maxMid=maxMid == null?0:maxMid;
        for (long i = maxMid+1; i < maxMid+count+1; i++) {
            String url = BiliUrlConstant.INFO_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
            spider.addUrl(url);
        }
//        HttpClientDownloader downloader = new HttpClientDownloader();
//        List<ProxyIp> list = proxyIpService.list(new QueryWrapper<ProxyIp>()
//                .orderByAsc("connect_speed")
//                .last("limit 1000"));
//        List<Proxy> proxyList = new ArrayList<>();
//        for (ProxyIp proxyIp : list) {
//            proxyList.add(new Proxy(proxyIp.getIp(), proxyIp.getPort()));
//        }
//        downloader.setProxyProvider(SimpleProxyProvider.from(proxyList.toArray(new Proxy[0])));
//        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("101.101.101.101",8888)));
//        spider.setDownloader(downloader);
        spider.start();
    }

    public void stop(){
        spider.stop();
    }

    public List<BiliUser> list() {
        List<BiliUser> list = biliUserRepository.findAll();
        return list;
    }
}
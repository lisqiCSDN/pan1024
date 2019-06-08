package com.pan1024.processor;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @ClassName: BaiduyunPageProcessor
 * @Date: 2019/6/6
 * @describe:
 */
@Component
public class BaiduyunPageProcessor implements PageProcessor {

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setRetryTimes(3)
            .setTimeOut(30000)
            .setSleepTime(2000)//60s内上限150次
            .setCycleRetryTimes(3)
            .setUseGzip(true)
            .addHeader("Accept","application/json, text/plain, */*")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Connection","keep-alive")
            .addHeader("Content-Type","application/json; charset=utf-8")
            .setCharset("UTF-8");
    private static String counta ="Ta还没有个人说明呢";

    @Override
    public void process(Page page) {
        String pageRawText = page.getRawText();
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(pageRawText);
        Integer code = JsonPath.read(document, "$.errno");
        if(code!=0){
            page.setSkip(true);
        }else {
            Integer uk = JsonPath.read(document, "$.user_info.uk");
            Integer follow_count = JsonPath.read(document, "$.user_info.follow_count");//关注
            Integer fans_count = JsonPath.read(document, "$.user_info.fans_count");//粉丝
            Integer album_count = JsonPath.read(document, "$.user_info.album_count");//专辑
            Integer pubshare_count = JsonPath.read(document, "$.user_info.pubshare_count");//分享
            String uname = JsonPath.read(document, "$.user_info.uname");//昵称
            String intro = JsonPath.read(document, "$.user_info.intro");//描述
            String avatar_url = JsonPath.read(document, "$.user_info.avatar_url");//头像
            page.putField("uk",uk);
            page.putField("follow_count",follow_count);
            page.putField("fans_count",fans_count);
            page.putField("album_count",album_count);
            page.putField("pubshare_count",pubshare_count);
            page.putField("uname",uname);
            page.putField("intro",intro);
            page.putField("avatar_url",avatar_url);
            page.putField("url",page.getUrl().get());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new BaiduyunPageProcessor()).addUrl("https://pan.baidu.com/pcloud/user/getinfo?query_uk=2016904429").thread(1).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");
    }
}

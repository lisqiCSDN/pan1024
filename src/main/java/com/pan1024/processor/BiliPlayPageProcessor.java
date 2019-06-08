package com.pan1024.processor;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @ClassName: BiliSpider
 * @Date: 2019/6/6
 * @describe: bilibili获取用户播放数
 */
@Component
public class BiliPlayPageProcessor implements PageProcessor {

    private Site site = Site.me()
            .setRetryTimes(3)
            .setTimeOut(30000)
            .setSleepTime(2000)
            .setCycleRetryTimes(3)
            .setUseGzip(true)
            .addHeader("Host","api.bilibili.com")
            .addHeader("Accept","application/json, text/plain, */*")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Connection","keep-alive")
            .addHeader("Content-Type","application/json; charset=utf-8");

    private static final long BEGIN_MID = 1;    //开始用户mid
    private static final long END_MID = 200;      //结束用户mid,(2017-04的估计注册用户数)

    @Override
    public void process(Page page) {
        String pageRawText = page.getRawText();
        //跳过连接失败页
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(pageRawText);
        Integer code = JsonPath.read(document, "$.code");
        if(code!=0){
            page.setSkip(true);
        }else {
            String url = page.getUrl().get();
            String mid = url.replace(url.substring(0, url.indexOf("mid=")+4), "").replace("&jsonp=jsonp", "");
            Integer play = JsonPath.read(document, "$.data.archive.view");

            page.putField("mid",mid);
            page.putField("play",play);
            page.putField("type",2);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    //运行主方法
    public static void main(String[] args) {
        Spider spider = Spider.create(new BiliPlayPageProcessor());
        //添加请求对象序列
        long mid;
        for (mid = BEGIN_MID; mid < END_MID; mid++) {
            String url = "https://api.bilibili.com/x/space/upstat?mid=" + mid + "&jsonp=jsonp";// bilibili获取用户播放数
            spider.addUrl(url);
        }
        spider.thread(3).run();//启动60个线程
    }
}

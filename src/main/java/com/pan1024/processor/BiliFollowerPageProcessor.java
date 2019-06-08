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
 * @describe: bilibili获取用户关注，粉丝数
 */
@Component
public class BiliFollowerPageProcessor implements PageProcessor {

    //构建Site对象，指定请求头键值字段
    private Site site = Site.me()
            .setRetryTimes(3)
            .setTimeOut(30000)
            .setSleepTime(2000)     //跟据试验，http://space.bilibili.com/ajax/member/GetInfo接口有IP接入限制，估计是60s内上限150次
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
            Integer mid = JsonPath.read(document, "$.data.mid");
            Integer following = JsonPath.read(document, "$.data.following");
            Integer follower = JsonPath.read(document, "$.data.follower");
            page.putField("mid",mid);
            page.putField("following",following);
            page.putField("follower",follower);
            page.putField("type",1);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    //运行主方法
    public static void main(String[] args) {
        Spider spider = Spider.create(new BiliFollowerPageProcessor());
        //添加请求对象序列
        long mid;
        for (mid = BEGIN_MID; mid < END_MID; mid++) {
            String url = "https://api.bilibili.com/x/relation/stat?vmid=" + mid + "&jsonp=jsonp";// bilibili获取用户关注，粉丝数
            spider.addUrl(url);
        }
        spider.thread(5).run();//启动60个线程
    }
}

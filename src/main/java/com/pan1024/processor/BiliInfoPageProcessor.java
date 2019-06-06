package com.pan1024.processor;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.pan1024.entity.BiliUser;
import com.pan1024.pipeline.BiliPipeline;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @ClassName: BiliInfoPageProcessor
 * @Date: 2019/6/6
 * @describe:  获取用户信息
 */
@Component
public class BiliInfoPageProcessor implements PageProcessor {

    /**
     * 构建Site对象，指定请求头键值字段
     */
    private Site site = Site.me()
            .setRetryTimes(3)
            .setTimeOut(30000)
            .setSleepTime(1500)     //跟据试验，http://space.bilibili.com/ajax/member/GetInfo接口有IP接入限制，估计是60s内上限150次
            .setCycleRetryTimes(3)
            .setUseGzip(true)
            .addHeader("Host","api.bilibili.com")
//            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0")
            .addHeader("Accept","application/json, text/plain, */*")
//            .addHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Connection","keep-alive")
            .addHeader("Content-Type","application/json; charset=utf-8");
//            .addHeader("Referer","https://space.bilibili.com/109770609/");

    private static final long BEGIN_MID = 1;    //开始用户mid
    private static final long END_MID = 4;      //结束用户mid,(2017-04的估计注册用户数)

    @Override
    public void process(Page page) {
        String pageRawText = page.getRawText();
        //跳过连接失败页
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(pageRawText);
        Integer code = JsonPath.read(document, "$.code");
        if(code!=0){
            page.setSkip(true);
        }
        //使用jsonPath获取json中的有效数据，并装载入BiliUser对象
        BiliUser user = new BiliUser();

        user.setMid(((Integer)JsonPath.read(document,"$.data.mid")).longValue());
        user.setName(JsonPath.read(document,"$.data.name"));
        user.setSex(JsonPath.read(document,"$.data.sex"));
        user.setLevel(((Integer)JsonPath.read(document,"$.data.level")));
        user.setSign(JsonPath.read(document,"$.data.sign"));
        user.setFace(JsonPath.read(document,"$.data.face"));
        user.setBirthday(JsonPath.read(document,"$.data.birthday"));
        page.putField("user",user);
        page.putField("type",0);
    }

    @Override
    public Site getSite() {
        return site;
    }

    //运行主方法
    public static void main(String[] args) {
        Spider spider = Spider.create(new BiliInfoPageProcessor());
        //添加请求对象序列
        long mid;
        for (mid = BEGIN_MID; mid < END_MID; mid++) {
            String url = "https://api.bilibili.com/x/space/acc/info?mid=" + mid + "&jsonp=jsonp";
            spider.addPipeline(new BiliPipeline()).addUrl(url);
        }
        //启动2个线程
        spider.thread(1).run();
    }
}

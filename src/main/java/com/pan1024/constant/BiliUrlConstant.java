package com.pan1024.constant;

public class BiliUrlConstant {

    public static String REPLACE_NAME = "MID";

    /**
     * bilibili获取用户信息接口
     */
    public static String INFO_URL = "https://api.bilibili.com/x/space/acc/info?mid=MID&jsonp=jsonp";
    /**
     * bilibili获取用户关注，粉丝数
     */
    public static String FOLLOWER_URL = "https://api.bilibili.com/x/relation/stat?vmid=MID&jsonp=jsonp";
    /**
     * bilibili获取用户播放数
     */
    public static String FLAY_URL = "https://api.bilibili.com/x/space/upstat?mid=MID&jsonp=jsonp";
}

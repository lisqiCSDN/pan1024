package com.pan1024.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName: BiliUser
 * @Date: 2019/6/6
 * @describe: bilibili用户
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bilibili_user")
public class BiliUser {
    @Id
    private long mid;    //mid
    private String name;   //昵称
    private String sex ;   //性别[“男”，“女”，“保密”]
    private int level;     //等级[1-5]
    private String sign;   //用户签名
    private String face;   //用户头像图片链接
    private int friends;   //关注数量
    private int fans;      //粉丝数量
    private int play;    //上传视频播放量
    private String birthday;   //生日（系统默认项：0000-01-01）
    private String place ;    //所在地点
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime ;    //收入时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDate;    //最后更新时间
}

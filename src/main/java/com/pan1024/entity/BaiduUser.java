package com.pan1024.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 百度分享用户
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "baidu_user")
public class BaiduUser {

    @Id
    private long uk;    //uk
    private String uname;   //昵称
    private String url;     //url
    private String icon;   //用户头像
    private String intro;   //说明
    private int share;     //分享
    private int album;     //专辑
    private int follow;   //关注数量
    private int fans;      //粉丝数量
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime ;    //收入时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDate;    //更新时间
}

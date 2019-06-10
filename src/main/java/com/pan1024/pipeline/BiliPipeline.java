package com.pan1024.pipeline;

import com.pan1024.entity.BiliUser;
import com.pan1024.repository.BiliUserRepository;
import com.pan1024.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class BiliPipeline implements Pipeline{

    @Autowired
    private BiliUserRepository biliUserRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Async("asyncServiceExecutor")
    @Override
    public void process(ResultItems resultItems, Task task) {
        int type = resultItems.get("type");
        if (type==0){
            BiliUser users = resultItems.get("user");
            String sql="INSERT INTO bilibili_user (birthday, create_time, face, " +
                    "level, name, sex, sign, mid) \n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] args={users.getBirthday(),DateUtil.getNow(),users.getFace(),users.getLevel(),users.getName(),
                    users.getSex(),users.getSign(),users.getMid()};
            log.info(sql);
            jdbcTemplate.update(sql,args);
        }else if (type==1){
            Integer mid = resultItems.get("mid");
            Integer following = resultItems.get("following");
            Integer follower = resultItems.get("follower");
            biliUserRepository.upFriendsFans(following,follower,mid,DateUtil.getNow());
        }else if (type==2){
            String mid = resultItems.get("mid");
            Integer play = resultItems.get("play");
            biliUserRepository.upPlay(play,Long.valueOf(mid),DateUtil.getNow());
        }
    }

}

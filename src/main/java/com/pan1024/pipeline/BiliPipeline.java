package com.pan1024.pipeline;

import com.pan1024.entity.BiliUser;
import com.pan1024.repository.BiliUserRepository;
import com.pan1024.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class BiliPipeline implements Pipeline{

    @Autowired
    private BiliUserRepository biliUserRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        int type = resultItems.get("type");
        if (type==0){
            BiliUser users = resultItems.get("user");
            users.setCreateTime(DateUtil.getNow());
            biliUserRepository.save(users);
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

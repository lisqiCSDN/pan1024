package com.pan1024.pipeline;

import com.pan1024.entity.BaiduUser;
import com.pan1024.repository.BaiduUserRepository;
import com.pan1024.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Transactional(rollbackFor = Exception.class)
public class BaiduPipeline implements Pipeline{

    @Autowired
    private BaiduUserRepository baiduUserRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        BaiduUser user = new BaiduUser();
        user.setUk(((Integer)resultItems.get("uk")).longValue());
        user.setFollow(resultItems.get("follow_count"));
        user.setFans(resultItems.get("fans_count"));
        user.setAlbum(resultItems.get("album_count"));
        user.setShare(resultItems.get("pubshare_count"));
        user.setUname(resultItems.get("uname"));
        user.setIntro(resultItems.get("intro"));
        user.setIcon(resultItems.get("avatar_url"));
        user.setUrl(resultItems.get("url"));
        user.setCreateTime(DateUtil.getNow());
        baiduUserRepository.save(user);
    }

}

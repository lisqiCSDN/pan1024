package com.pan1024.pipeline;

import com.pan1024.repository.BaiduUserRepository;
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
public class BaiduPipeline implements Pipeline{

    @Autowired
    private BaiduUserRepository baiduUserRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Async("asyncServiceExecutor")
    @Override
    public void process(ResultItems resultItems, Task task) {
        String sql="insert into baidu_user (album, create_time, fans, follow, icon, intro, share, uname, url, uk) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] args={resultItems.get("album_count"), DateUtil.getNow(),resultItems.get("fans_count"),resultItems
                .get("follow_count"),resultItems.get("avatar_url"),resultItems.get("intro"),resultItems.get
                ("pubshare_count"),resultItems.get("uname"),resultItems.get("url"),resultItems.get("uk")};
        log.info(sql);
        jdbcTemplate.update(sql,args);
    }

}

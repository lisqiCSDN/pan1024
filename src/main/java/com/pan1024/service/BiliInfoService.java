package com.pan1024.service;

import com.pan1024.constant.BiliUrlConstant;
import com.pan1024.entity.BiliUser;
import com.pan1024.pipeline.BiliPipeline;
import com.pan1024.processor.BiliInfoPageProcessor;
import com.pan1024.repository.BiliUserRepository;
import com.pan1024.vo.ResultPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SpiderService
 * @Date: 2019/6/6
 * @describe: 获取用户信息
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BiliInfoService {
    @Autowired
    private BiliPipeline biliPipeline;
    @Autowired
    private BiliInfoPageProcessor biliInfoPageProcessor;
    @Autowired
    private BiliUserRepository biliUserRepository;
    @Autowired
    private BiliFollowerService biliFollowerService;
    @Autowired
    private BiliPlayService biliFlayService;

    private Spider spider;
    @PostConstruct
    private void init(){
        spider = Spider.create(biliInfoPageProcessor)
                .addPipeline(biliPipeline);
    }

    @Async
    public void infoStart(Integer thread,Integer count){
        Long maxMid = biliUserRepository.maxMid();
        maxMid=maxMid==null?1:maxMid+1;
        for (long i = maxMid; i < maxMid+count; i++) {
            String url = BiliUrlConstant.INFO_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(i));
            spider.addUrl(url);
        }
//        HttpClientDownloader downloader = new HttpClientDownloader();
//        List<ProxyIp> list = proxyIpService.list(new QueryWrapper<ProxyIp>()
//                .orderByAsc("connect_speed")
//                .last("limit 1000"));
//        List<Proxy> proxyList = new ArrayList<>();
//        for (ProxyIp proxyIp : list) {
//            proxyList.add(new Proxy(proxyIp.getIp(), proxyIp.getPort()));
//        }
//        downloader.setProxyProvider(SimpleProxyProvider.from(proxyList.toArray(new Proxy[0])));
//        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("101.101.101.101",8888)));
//        spider.setDownloader(downloader);
        spider.thread(thread).start();
//        biliFollowerService.start(thread,count,maxMid);
//        biliFlayService.start(thread,count,maxMid);
    }

    @Async
    public void againStart(List<Long> mids){
        try {
            if (mids.size()>0){
                for (Long i=0L;i<mids.size();i++){
                    String url = BiliUrlConstant.INFO_URL.replace(BiliUrlConstant.REPLACE_NAME, String.valueOf(mids.get
                            (i.intValue())));
                    spider.addUrl(url);
                }
                spider.thread(1).run();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void stop(){
        spider.stop();
    }

    public ResultPageVO<BiliUser> list(Integer page, Integer pageSize, String search, Long mid) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<BiliUser> all = biliUserRepository.findAll(new Specification<BiliUser>() {
            @Override
            public Predicate toPredicate(Root<BiliUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
                    criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(1);
                if (StringUtils.isNotBlank(search)){
                    predicates.add(criteriaBuilder.like(root.get("name"), "%"+search+"%"));
                }
                if (mid!=null){
                    predicates.add(criteriaBuilder.equal(root.get("mid"), mid));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
        return new ResultPageVO<BiliUser>().success(all.getContent(),all.getTotalElements());
    }

    public List<Long> findVacancy(Long begin, Long count) {
        List<BigInteger> ids = biliUserRepository.findVacancy(begin,begin+count-1);
        List<Long> mid=new ArrayList<>();
        for (Long i=begin;i<begin+count;i++){
            mid.add(i);
        }
        List<Long> list = ids.parallelStream().map(BigInteger::longValue).collect(Collectors.toList());
        mid.removeAll(list);
        return mid;
    }
}
package com.pan1024.service;

import com.pan1024.constant.BaiduUrlConstant;
import com.pan1024.entity.BaiduUser;
import com.pan1024.pipeline.BaiduPipeline;
import com.pan1024.processor.BaiduyunPageProcessor;
import com.pan1024.repository.BaiduUserRepository;
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
 * @describe: 百度获取用户数
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaiduService {
    @Autowired
    private BaiduPipeline biliPipeline;
    @Autowired
    private BaiduyunPageProcessor baiduyunPageProcessor;
    @Autowired
    private BaiduUserRepository baiduUserRepository;

    private Spider spider;
    @PostConstruct
    private void init(){
        spider = Spider.create(baiduyunPageProcessor)
                .addPipeline(biliPipeline);
    }

    @Async("asyncServiceExecutor")
    public void baiduStart(Integer count){
        try {
            Long maxMid = baiduUserRepository.maxMid();
            if (maxMid==null){
                return ;
            }
            for (long i=maxMid;i<maxMid+count;i++){
                String url = BaiduUrlConstant.INFO_URL.replace(BaiduUrlConstant.REPLACE_NAME, String.valueOf(i));
                spider.addUrl(url);
            }
            spider.thread(1).run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Async("asyncServiceExecutor")
    public void againStart(List<Long> uks){
        try {
            if (uks.size()>0){
                for (Long i=0L;i<uks.size();i++){
                    String url = BaiduUrlConstant.INFO_URL.replace(BaiduUrlConstant.REPLACE_NAME, String.valueOf(uks.get
                            (i.intValue())));
                    spider.addUrl(url);
                }
                spider.thread(1).run();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void baiduStop(){
        spider.stop();
    }

    public ResultPageVO<BaiduUser> list(Integer page, Integer pageSize, String search, Long uk) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<BaiduUser> all = baiduUserRepository.findAll(new Specification<BaiduUser>() {
            @Override
            public Predicate toPredicate(Root<BaiduUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
                    criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(1);
                if (StringUtils.isNotBlank(search)){
                    predicates.add(criteriaBuilder.like(root.get("uname"), "%"+search+"%"));
                }
                if (uk!=null){
                    predicates.add(criteriaBuilder.equal(root.get("uk"), uk));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
        return new ResultPageVO<BaiduUser>().success(all.getContent(),all.getTotalElements());
    }

    public List<Long> findVacancy(Long begin, Long count) {
        List<BigInteger> ids = baiduUserRepository.findVacancy(begin,begin+count-1);
        List<Long> uk=new ArrayList<>();
        for (Long i=begin;i<begin+count;i++){
            uk.add(i);
        }
        List<Long> list = ids.parallelStream().map(BigInteger::longValue).collect(Collectors.toList());
        uk.removeAll(list);
        return uk;
    }
}
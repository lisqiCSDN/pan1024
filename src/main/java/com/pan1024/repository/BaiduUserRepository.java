package com.pan1024.repository;

import com.pan1024.entity.BaiduUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface BaiduUserRepository extends JpaRepository<BaiduUser, Long>,JpaSpecificationExecutor<BaiduUser> {

    @Query(value = "select max(uk) from baidu_user", nativeQuery = true)
    Long maxMid();
}

package com.pan1024.repository;

import com.pan1024.entity.BaiduUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface BaiduUserRepository extends JpaRepository<BaiduUser, Long>,JpaSpecificationExecutor<BaiduUser> {

    @Query(value = "select max(uk) from baidu_user where uk between 20000000 and 30000000-1", nativeQuery = true)
    Long maxMid();

    @Query(value = "select uk from baidu_user where uk between ?1 and ?2", nativeQuery = true)
    List<BigInteger> findVacancy(Long begin, Long count);
}

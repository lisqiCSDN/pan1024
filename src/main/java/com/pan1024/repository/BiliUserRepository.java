package com.pan1024.repository;

import com.pan1024.entity.BiliUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface BiliUserRepository extends JpaRepository<BiliUser, Long>,JpaSpecificationExecutor<BiliUser> {

    @Modifying(clearAutomatically = true)
    @Query(value = "update bilibili_user set friends = ?1,fans=?2,fans_last_date=?4,fans_status=1 where mid=?3",nativeQuery = true)
    void upFriendsFans(int friends, int fans, long mid, Date date);

    @Modifying(clearAutomatically = true)
    @Query(value = "update bilibili_user set play = ?1,play_last_date=?3,play_status=1 where mid=?2",nativeQuery = true)
    void upPlay(int play,long mid, Date date);

    @Query(value = "select max(mid) from bilibili_user where mid between 20000000 and 30000000-1", nativeQuery = true)
    Long maxMid();

    @Query(value = "select min(mid) from bilibili_user where fans_status=0", nativeQuery = true)
    Long fansMinMid();

    @Query(value = "select min(mid) from bilibili_user where play_status=0", nativeQuery = true)
    Long playMinMid();

    @Query(value = "select mid from bilibili_user where mid between ?1 and ?2", nativeQuery = true)
    List<BigInteger> findVacancy(Long begin, Long count);
}

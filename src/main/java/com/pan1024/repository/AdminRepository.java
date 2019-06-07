package com.pan1024.repository;

import com.pan1024.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 后台管理账号
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {

    Optional<Admin> findByName(String userName);

    Optional<Admin> findByNameAndPassword(String userName, String password);

    @Modifying
    @Query(value = "update admin a set a.password=?2 where a.id=?1", nativeQuery = true)
    void updatePassword(Long id, String newPassword);

    @Modifying
    @Query(value = "delete from admin where id=?1", nativeQuery = true)
    void delAdmin(Long id);
}

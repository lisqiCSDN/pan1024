package com.pan1024.service;

import com.pan1024.entity.Admin;
import com.pan1024.repository.AdminRepository;
import com.pan1024.util.DateUtil;
import com.pan1024.util.MD5Util;
import com.pan1024.vo.ResultOneVO;
import com.pan1024.vo.ResultPageVO;
import com.pan1024.vo.ResultVoidVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 后台管理
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminService {

    @Autowired private AdminRepository adminRepository;

    public ResultPageVO<Admin> findAll(Integer page, Integer limit) {
        PageRequest request = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "createTime");
        Page<Admin> all = adminRepository.findAll(new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                return query.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        }, request);
        return new ResultPageVO<Admin>().success(all.getContent(), all.getTotalElements());
    }

    public ResultVoidVO updatePassword(Long id, String newPassword) {
        adminRepository.updatePassword(id, MD5Util.encode(newPassword));
        return new ResultVoidVO().success();
    }

    public ResultOneVO<Admin> add(String name, String password) {
        ResultOneVO<Admin> vo = new ResultOneVO<>();
        Optional<Admin> optional = adminRepository.findByName(name);
        if (optional.isPresent()) {
            vo.setMsg("用户名已存在");
        } else {
            Admin newA = new Admin();
            newA.setName(name);
            newA.setPassword(MD5Util.encode(password));
            newA.setIcon(Admin.defaultIcon);
            newA.setCreateTime(DateUtil.getNow());
            Admin admin = adminRepository.saveAndFlush(newA);
            vo.success(admin);
        }
        return vo;
    }

    public ResultVoidVO delAdmin(Long id) {
        adminRepository.delAdmin(id);
        return new ResultVoidVO().success();
    }

    public ResultVoidVO login(String name, String password, HttpServletRequest request) {
        ResultVoidVO vo=new ResultVoidVO();
        Optional<Admin> admin = adminRepository.findByNameAndPassword(name, MD5Util.encode(password));
        if (admin.isPresent()){
            request.getSession().setAttribute("admin",admin.get());
            vo.success();
        }else {
            vo.fail("100001","账号或密码错误");
        }
        return vo;
    }
}

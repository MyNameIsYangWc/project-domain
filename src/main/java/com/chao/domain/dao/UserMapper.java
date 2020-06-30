package com.chao.domain.dao;

import com.chao.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(String user);

    int insertSelective(User record);

    User selectByPrimaryKey(String user);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKeyWithBLOBs(User record);

    int updateByPrimaryKey(User record);
}
package com.chao.domain.dao;

import com.chao.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void insertUser(User user);

    User selectUser(User user);
}

package com.chao.domain.service;

import com.chao.domain.model.User;
import com.chao.domain.result.Result;

public interface UserService {

    Result insertUser(User user);

    Result userLogin(User user);
}

package com.chao.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.chao.domain.model.User;
import com.chao.domain.result.Result;

public interface UserService {

    Result insertUser(User user);

    Result userLogin(User user);

    Result logout(String username);

    Result resetPwd(JSONObject user);
}

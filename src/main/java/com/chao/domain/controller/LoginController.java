package com.chao.domain.controller;

import com.chao.domain.model.User;
import com.chao.domain.result.Result;
import com.chao.domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "LoginController",description = "登录")
public class LoginController {

    private Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService uerService;


    /**
     * 用户登录
     */
    @ApiOperation(value = "用户登录",notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "用户信息",required = true,dataType = "User",paramType = "body"),
    })
    @PostMapping("/login")
    public Result userLogin(@RequestBody User user){

        logger.info("###userLogin参数为：###"+user);
        Result result = uerService.userLogin(user);

        return result;
    }

    /**
     * 新增用户
     */
    @ApiOperation(value = "新增用户",notes = "新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "用户信息",required = true,dataType = "User",paramType = "body"),
    })
    @PostMapping("/insertUser")
    public Result insertUser(@RequestBody User user){

        logger.info("###insertUser参数为：###"+user);
        Result result = uerService.insertUser(user);

        return result;
    }


}

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
import org.springframework.web.bind.annotation.*;

/**
 * 登录处理
 * @author 杨文超
 * @date 2020-07-02
 */
@RestController
@Api(value = "LoginController",description = "登录")
public class LoginController {

    private Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService uerService;

    /**
     * 用户登录
     * @author 杨文超
     * @date 2020-06-27
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
     * 退出
     * @param username
     * @author 杨文超
     * @date 2020-06-29
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "账号",required = true,dataType = "String",paramType = "query"),

            @ApiImplicitParam(name = "Authorization",value = "token",required = true,dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "Accept",value = "",required = false,dataType = "String",paramType = "header",defaultValue = "application/json")
    })
    @GetMapping("/userlogout")
    public Result logout(@RequestParam String username){

        logger.info("###userlogout参数为：###"+username);
        Result result = uerService.logout(username);
        return result;
    }
}

package com.chao.domain.controller;

import com.alibaba.fastjson.JSONObject;
import com.chao.domain.result.Result;
import com.chao.domain.result.ResultCode;
import com.chao.domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户中心
 * @author 杨文超
 * @date 2020-07-02
 */
@RestController
@Api(value = "UserController",description = "用户中心")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService uerService;

    /**
     * 重置密码
     * user：账号，旧密码，新密码
     * @author 杨文超
     * @date 2020-07-01
     */
    @ApiOperation(value = "重置密码",notes = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "用户信息",required = true,dataType = "JSONObject",paramType = "body"),

            @ApiImplicitParam(name = "Authorization",value = "token",required = true,dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "Accept",value = "",required = false,dataType = "String",paramType = "header",defaultValue = "application/json")
    })
    @PostMapping("/resetPwd")
    public Result resetPwd(@RequestBody JSONObject user){
        if(StringUtils.isBlank(user.getString("username")) ||
                StringUtils.isBlank(user.getString("oldPassword")) || StringUtils.isBlank(user.getString("newPassword"))){
            return new Result(ResultCode.businErrorCode.getCode(),"参数错误");
        }
        Result result = uerService.resetPwd(user);
        return result;
    }
}

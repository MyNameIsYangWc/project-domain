package com.chao.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chao.domain.common.Constants;
import com.chao.domain.dao.AttachmentMapper;
import com.chao.domain.dao.UserMapper;
import com.chao.domain.model.Attachment;
import com.chao.domain.model.User;
import com.chao.domain.result.Result;
import com.chao.domain.result.ResultCode;
import com.chao.domain.service.UserService;
import com.chao.domain.token.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.chao.domain.utils.SecurityUtils.pwdSecurity;
import static com.chao.domain.utils.SecurityUtils.verifyPwd;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private  JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ConcurrentHashMap<String,UserDetails> userDetailMap;

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public Result userLogin(User user) {

        HttpServletResponse response= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //根据账号获取用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        //密码校验
        if(!verifyPwd(user.getPassword(),userDetails.getPassword())){
            return new Result(ResultCode.businErrorCode.getCode(),"密码不正确");
        }
        String token= jwtTokenUtil.generateToken(userDetails);//生成token
        redisTemplate.opsForValue().set(userDetails.getUsername(),token, Constants.TOKEN_EXP, TimeUnit.HOURS);//用户token存入redis,已登录过的用户被退出
        response.setHeader("token",token);
        //生成用户信息返回
        user.setUsername(userDetails.getUsername());
        user.setPassword(null);
        user.setAuthorities(userDetails.getAuthorities());
        user.setEnabled(userDetails.isEnabled());
        //查询用户头像fileId
        Attachment attachment = attachmentMapper.selectUserImage(user.getUsername());
        attachmentMapper.selectUserImage(user.getUsername());
        user.setFileId(attachment.getFileId());
        return new Result(ResultCode.successCode.getCode(),ResultCode.successCode.getMsg(),user);
    }

    /**
     * 退出
     * @param username
     * @author 杨文超
     * @date 2020-06-29
     */
    @Override
    public Result logout(String username) {

        Boolean isLogout = redisTemplate.delete(username);
        userDetailMap.remove(username);
        if(isLogout){
            return new Result(ResultCode.successCode.getCode(),ResultCode.successCode.getMsg());
        }
        return new Result(ResultCode.businErrorCode.getCode(),"用户已失效/用户未登录");
    }

    /**
     * 重置密码
     * user：账号，旧密码，新密码
     * @author 杨文超
     * @date 2020-07-01
     */
    @Override
    public Result resetPwd(JSONObject user) {

        String username = user.getString("username");
        String oldPwdSecurity = pwdSecurity(user.getString("oldPassword"));//老密码加密
        String newPwdSecurity = pwdSecurity(user.getString("newPassword")); //新密码加密

        //根据账号获取用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //密码校验
        if(!verifyPwd(oldPwdSecurity,userDetails.getPassword())){
            return new Result(ResultCode.businErrorCode.getCode(),"密码不正确");
        }
        //校验新老密码是否一致
        if(verifyPwd(oldPwdSecurity,newPwdSecurity)){
            return new Result(ResultCode.businErrorCode.getCode(),"新旧密码不能重复使用");
        }
       //重置密码
        userMapper.updatePWD(username,newPwdSecurity);
        //删除token重新登录
        redisTemplate.delete(username);
        userDetailMap.remove(username);
        return new Result(ResultCode.successCode.getCode(),ResultCode.successCode.getMsg());
    }

    /**
     * 注册账号
     * @param user
     * @author 杨文超
     * @date 2020-07-07
     */
    @Override
    public Result registerUser(User user) {
        //根据账号获取用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        if(userDetails != null){
            return new Result(ResultCode.businErrorCode.getCode(),"账号存在");
        }
        userMapper.insertSelective(user);
        return new Result(ResultCode.successCode.getCode(),"注册成功");
    }

}

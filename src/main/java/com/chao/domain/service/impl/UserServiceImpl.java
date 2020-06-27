package com.chao.domain.service.impl;

import com.chao.domain.common.SecurityUtils;
import com.chao.domain.dao.UserMapper;
import com.chao.domain.model.User;
import com.chao.domain.result.Result;
import com.chao.domain.result.ResultCode;
import com.chao.domain.service.UserService;
import com.chao.domain.token.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private  JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    private JdbcDaoImpl jdbcDao;

    @Autowired
    public UserServiceImpl(DataSource dataSource) {
        jdbcDao = new JdbcDaoImpl();
        jdbcDao.setDataSource(dataSource);
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public Result userLogin(User user) {

        //根据账号获取用户信息
        UserDetails userDetails = jdbcDao.loadUserByUsername(user.getUsername());
        //密码校验
        if(!SecurityUtils.verifyPwd(user.getPassword(),userDetails)){
            return new Result(ResultCode.businErrorCode.getCode(),"密码不正确");
        }
        String token= jwtTokenUtil.generateToken(userDetails);//生成token
        redisTemplate.opsForValue().set(userDetails.getUsername(),token,1, TimeUnit.HOURS);//用户token存入redis,已登录过的用户被退出
        //生成用户信息返回
        user.setUsername(userDetails.getUsername());
        user.setToken(token);
        user.setPassword(null);
        user.setAuthorities(userDetails.getAuthorities());
        return new Result(ResultCode.successCode.getCode(),ResultCode.successCode.getMsg(),user);
    }

    /**
     * 查询用户信息
     */
    public Result selectUserInformation(User user) {

        User user1= userMapper.selectUser(user);
        if(user1 !=null){
            return new Result(ResultCode.successCode.getCode(),"成功");
        }

        return new Result(ResultCode.businErrorCode.getCode(),"查询用户信息失败");
    }

    /**
     * 注册账号
     * @param user
     */
    @Override
    public Result insertUser(User user) {

        return new Result(ResultCode.businErrorCode.getCode(),"账号存在");
    }



}

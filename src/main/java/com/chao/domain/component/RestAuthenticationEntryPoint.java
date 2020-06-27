package com.chao.domain.component;

//import cn.hutool.json.JSONUtil;
//import com.macro.mall.common.api.CommonResult;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.chao.domain.result.Result;
import com.chao.domain.result.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：未登录或登录过期
 * Created by macro on 2018/5/14.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONObject.toJSON(new Result(ResultCode.InvalidToken.getCode(),ResultCode.InvalidToken.getMsg())));
        response.getWriter().flush();
    }
}

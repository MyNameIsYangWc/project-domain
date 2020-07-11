package com.chao.domain.oauth2Config;

import com.chao.domain.thread.MyThreadPoolExecutor;
import com.chao.domain.token.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.chao.domain.common.Constants.REFRESH_TOKEN;
import static com.chao.domain.common.Constants.TOKEN_EXP;

/**
 * JWT登录授权过滤器
 * @auther 杨文超
 * @date 2020/6/27
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ConcurrentHashMap<String,UserDetails> userDetailMap;//存储登录用户 UserDetails 信息,token失效,此处也需删除

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * token校验
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     * @auther 杨文超
     * @date 2020/07/07
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);//获取请求头中的Token
        Boolean flag=false; //执行流程校验标记
        String username="";//账号
        Object o1=null;//刷新token标记
        ValueOperations ops = redisTemplate.opsForValue();

        if (authHeader != null && authHeader.startsWith(this.tokenHead)) { //token 格式校验
            flag=true;
        }
        if(flag){
            //通过token获取用户名
            String authToken = authHeader.substring(this.tokenHead.length());
            username = jwtTokenUtil.getUserNameFromToken(authToken);
            flag=username==null?false:true;
            if (flag) {
                String token = ops.get(username).toString();//根据用户名获取token
                flag = token==null ? false : jwtTokenUtil.compareToken(authToken.trim(), token.trim());//token校验
                o1 = ops.get(authHeader);
            }
        }
        //刷新token
        if(flag && o1 == null){
            final String user= username;
            MyThreadPoolExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    redisTemplate.expire(user, TOKEN_EXP,TimeUnit.HOURS);//重置token有效期
                    ops.set(authHeader,TOKEN_EXP,REFRESH_TOKEN, TimeUnit.MINUTES);//设置刷新token间隔时间
                }
            });
        }
        if(flag){
            UserDetails userDetails = userDetailMap.get(username);//根据账号从内存获取userDetails信息
            userDetails= userDetails!=null ? userDetails : userDetailsService.loadUserByUsername(username);//数据库查询
            //认证放行
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}

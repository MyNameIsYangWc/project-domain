package com.chao.domain.oauth2Config;

import com.chao.domain.token.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private ConcurrentHashMap<String,UserDetails> userDetailMap;//存储登录用户 UserDetails 信息,token失效,此处也需删除

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
        String username=null;//账号

        if (authHeader != null && authHeader.startsWith(this.tokenHead)) { //token 格式校验
            flag=true;
        }
        if(flag){
            //通过token获取用户名
            String authToken = authHeader.substring(this.tokenHead.length());
            username = jwtTokenUtil.getUserNameFromToken(authToken);
            flag=username==null?false:true;
            if (flag) {
                String token = redisTemplate.opsForValue().get(username).toString();//根据用户名获取token
                flag=token==null?false:jwtTokenUtil.compareToken(authToken.trim(),token.trim());//token校验
            }
            if(flag){//判断token是否失效
                Boolean tokenExp = jwtTokenUtil.isTokenExpired(authToken.trim());
                if(tokenExp){
                    //刷新token
                    String token = jwtTokenUtil.refreshHeadToken(authToken);
                    response.setHeader("token",token);
                }
            }
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

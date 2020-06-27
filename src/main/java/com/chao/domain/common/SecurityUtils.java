package com.chao.domain.common;

import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 */
public class SecurityUtils {

    /**
     * 密码SHA加密
     * @param str
     * @return
     */
    public static String pwdSecurity(String str) {
        String security =null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(str.getBytes());
            security = new BigInteger(sha.digest()).toString(32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return security;
    }

    /**
     * 密码校验
     * @param pwd
     * @param userDetails
     * @return
     */
    public static boolean verifyPwd(String pwd, UserDetails userDetails){
        String password = userDetails.getPassword();
        if(password.equals(pwd)){
            return true;
        }
        return false;
    }

}

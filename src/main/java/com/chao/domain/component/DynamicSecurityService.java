package com.chao.domain.component;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 动态权限相关业务类
 * Created by macro on 2020/2/7.
 */
@Component
public interface DynamicSecurityService {
    /**
     * 加载资源ANT通配符和资源对应MAP
     */
    Map<String, ConfigAttribute> loadDataSource();
}

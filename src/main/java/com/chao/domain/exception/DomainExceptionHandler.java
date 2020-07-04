package com.chao.domain.exception;

import com.chao.domain.result.Result;
import com.chao.domain.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 全局异常捕捉处理
 * @author 杨文超
 * @date 2020-06-27
 */
@RestControllerAdvice
public class DomainExceptionHandler{

    private Logger logger= LoggerFactory.getLogger(DomainExceptionHandler.class);

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
//        model.addAttribute("Data", model);
    }

    /**
     * 全局异常捕捉处理
     * @param e
     * @return
     * @author 杨文超
     * @date 2020-06-27
     */
    @ExceptionHandler(Exception.class)
    public Result errorHandler(Exception e) {
        logger.error("系统异常，错误信息：",e);
        return new Result(ResultCode.SystemErrorCode.getCode(),ResultCode.SystemErrorCode.getMsg(),e.getMessage());
    }
}

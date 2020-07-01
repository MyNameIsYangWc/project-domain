package com.chao.domain.controller;

import com.chao.domain.model.Attachment;
import com.chao.domain.result.Result;
import com.chao.domain.service.AttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 附件表操作
 * @author 杨文超
 * @date 2020-06-30
 */
@RestController
@Api(value = "AttachmentController",description = "附件表操作")
@RequestMapping("/attachment")
public class AttachmentController {

    private Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 用户头像上传
     * @param attachment
     * @author 杨文超
     * @date 2020-06-30
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attachment",value = "附件信息",required = true,dataType = "Attachment",paramType = "body"),

            @ApiImplicitParam(name = "Authorization",value = "token",required = true,dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "Accept",value = "",required = false,dataType = "String",paramType = "header",defaultValue = "application/json")
    })
    @PostMapping("/upload")
    public Result upload(@RequestBody Attachment attachment){
        logger.info("upload"+attachment.getUsername());
        return attachmentService.upload(attachment);
    }
}

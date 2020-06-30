package com.chao.domain.service.impl;

import com.chao.domain.common.Constants;
import com.chao.domain.dao.AttachmentMapper;
import com.chao.domain.model.Attachment;
import com.chao.domain.result.Result;
import com.chao.domain.result.ResultCode;
import com.chao.domain.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    /**
     * 用户头像上传
     * @param attachment
     * @author 杨文超
     * @date 2020-06-30
     */
    @Override
    public Result upload(Attachment attachment) {

        //判断用户是否上传过图片
        Attachment attachment1 = attachmentMapper.selectUserImage(attachment.getUsername());
        if(attachment1 != null){
            attachment1.setDelFlag(true);
            attachmentMapper.updateByPrimaryKeySelective(attachment1);
        }
        //附件信息
        attachment.setFileTypeCd(Constants.FILE_TYPE_CD_ONE);
        attachment.setFileTypeName(Constants.FILE_TYPE_NAME_ONE);
        attachment.setDelFlag(false);
        attachmentMapper.insertSelective(attachment);
        return new Result(ResultCode.successCode.getCode(),ResultCode.successCode.getMsg());
    }
}

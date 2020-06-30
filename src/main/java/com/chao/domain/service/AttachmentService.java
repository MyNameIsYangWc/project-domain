package com.chao.domain.service;

import com.chao.domain.model.Attachment;
import com.chao.domain.result.Result;

public interface AttachmentService {

    Result upload(Attachment attachment);
}

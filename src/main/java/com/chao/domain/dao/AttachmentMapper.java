package com.chao.domain.dao;

import com.chao.domain.model.ttachment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ttachment record);

    int insertSelective(ttachment record);

    ttachment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ttachment record);

    int updateByPrimaryKey(ttachment record);

    String selectUserImage(@Param("username") String username);
}

package com.gong.blog.manage.mapper;

import com.gong.blog.manage.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysOperLogMapper {

    int insertBatch(List<SysOperLog> sysOperLogList);

    int insertOne(SysOperLog sysOperLog);

    int deleteById(Long id);

    int deleteByIds(List<Long> ids);

    SysOperLog selectById(Long id);

    List<SysOperLog> selectList(SysOperLog sysOperLog);

    int updateById(SysOperLog sysOperLog);
}

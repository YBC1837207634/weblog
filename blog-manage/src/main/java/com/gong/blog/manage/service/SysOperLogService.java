package com.gong.blog.manage.service;

import com.gong.blog.manage.entity.SysOperLog;

import java.util.List;


public interface SysOperLogService {

    SysOperLog getById(Long id);

    List<SysOperLog> getList(SysOperLog sysOperLog);

    int saveOne(SysOperLog sysOperLog);

    int saveBatch(List<SysOperLog> sysOperLogs);

    int updateSysOperLogById(SysOperLog sysOperLog);

    int removeById(Long id);

    int removeByIds(List<Long> ids);

}

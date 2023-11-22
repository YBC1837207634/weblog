package com.gong.blog.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.gong.blog.manage.entity.SysOperLog;
import com.gong.blog.manage.mapper.SysOperLogMapper;
import com.gong.blog.manage.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Override
    public SysOperLog getById(Long id) {
        return sysOperLogMapper.selectById(id);
    }

    @Override
    public List<SysOperLog> getList(SysOperLog sysOperLog) {
        if (Objects.isNull(sysOperLog))
            sysOperLog = new SysOperLog();
        if (Objects.nonNull(sysOperLog.getPage()) && Objects.nonNull(sysOperLog.getPageSize()))
            PageHelper.startPage(sysOperLog.getPage(), sysOperLog.getPageSize());
        return sysOperLogMapper.selectList(sysOperLog);
    }

    @Override
    public int saveOne(SysOperLog sysOperLog) {
        return sysOperLogMapper.insertOne(sysOperLog);
    }

    @Override
    public int saveBatch(List<SysOperLog> sysOperLog) {
        return sysOperLogMapper.insertBatch(sysOperLog);
    }

    @Override
    public int updateSysOperLogById(SysOperLog sysOperLog) {
        return sysOperLogMapper.updateById(sysOperLog);
    }

    @Override
    public int removeById(Long id) {
        return sysOperLogMapper.deleteById(id);
    }

    @Override
    public int removeByIds(List<Long> ids) {
        return sysOperLogMapper.deleteByIds(ids);
    }
}

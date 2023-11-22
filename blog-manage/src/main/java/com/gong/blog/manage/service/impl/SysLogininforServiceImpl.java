package com.gong.blog.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.gong.blog.manage.entity.SysLogininfor;
import com.gong.blog.manage.mapper.SysLogininforMapper;
import com.gong.blog.manage.service.SysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SysLogininforServiceImpl implements SysLogininforService {

    @Autowired
    private SysLogininforMapper sysLogininforMapper;

    @Override
    public SysLogininfor getById(Long id) {
        return sysLogininforMapper.selectById(id);
    }

    @Override
    public List<SysLogininfor> getList(SysLogininfor sysLogininfor) {
        if (Objects.isNull(sysLogininfor))
            sysLogininfor = new SysLogininfor();
        if (Objects.nonNull(sysLogininfor.getPage()) && Objects.nonNull(sysLogininfor.getPageSize()))
            PageHelper.startPage(sysLogininfor.getPage(), sysLogininfor.getPageSize());
        return sysLogininforMapper.selectList(sysLogininfor);
    }

    @Override
    public int saveOne(SysLogininfor sysLogininfor) {
        return sysLogininforMapper.insertOne(sysLogininfor);
    }

    @Override
    public int saveBatch(List<SysLogininfor> sysLogininfor) {
        return sysLogininforMapper.insertBatch(sysLogininfor);
    }

    @Override
    public int updateSysLogininforById(SysLogininfor sysLogininfor) {
        return sysLogininforMapper.updateById(sysLogininfor);
    }

    @Override
    public int removeById(Long id) {
        return sysLogininforMapper.deleteById(id);
    }

    @Override
    public int removeByIds(List<Long> ids) {
        return sysLogininforMapper.deleteByIds(ids);
    }
}

package com.gong.blog.manage.mapper;

import com.gong.blog.manage.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    int insertBatch(List<SysMenu> sysMenuList);

    int insertOne(SysMenu sysMenu);

    int deleteById(Long id);

    int deleteByIds(List<Long> ids);

    SysMenu selectById(Long id);

    List<SysMenu> selectList(SysMenu sysMenu);

    int updateById(SysMenu sysMenu);

    List<SysMenu> selectMenuByUserIdAndType(Long id, String... menuTypes);

    List<SysMenu> selectMenuByRoleIdType(Long roleId);
}

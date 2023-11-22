package com.gong.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.blog.common.entity.Relation;
import com.gong.blog.common.entity.User;
import com.gong.blog.common.params.RelationParams;

import java.util.List;

/**
* @author asus
* @description 针对表【relation】的数据库操作Mapper
* @createDate 2023-10-31 19:51:25
* @Entity com.gong.blog.common.entity.Relation
*/
public interface RelationMapper extends BaseMapper<Relation> {
    List<User> getGoalList(RelationParams params);
}





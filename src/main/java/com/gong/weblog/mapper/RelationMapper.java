package com.gong.weblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gong.weblog.dto.RelationParams;
import com.gong.weblog.entity.Relation;
import com.gong.weblog.entity.User;

import java.util.List;

/**
* @author asus
* @description 针对表【relation】的数据库操作Mapper
* @createDate 2023-10-31 19:51:25
* @Entity com.gong.weblog.entity.Relation
*/
public interface RelationMapper extends BaseMapper<Relation> {
    List<User> getGoalList(RelationParams params);
}





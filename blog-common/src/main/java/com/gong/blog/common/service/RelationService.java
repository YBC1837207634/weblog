package com.gong.blog.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.entity.Relation;
import com.gong.blog.common.form.RelationForm;
import com.gong.blog.common.params.RelationParams;
import com.gong.blog.common.vo.UserVo;

/**
* @author asus
* @description 针对表【relation】的数据库操作Service
* @createDate 2023-10-31 19:51:25
*/
public interface RelationService extends IService<Relation> {
    void link(RelationForm form);

    IPage<UserVo> getGoalPage(RelationParams params) ;

    boolean queryRelation(Long goalId);

}

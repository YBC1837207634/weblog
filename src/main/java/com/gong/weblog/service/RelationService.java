package com.gong.weblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.weblog.dto.PageParams;
import com.gong.weblog.dto.RelationForm;
import com.gong.weblog.entity.Relation;
import com.gong.weblog.vo.UserVo;

/**
* @author asus
* @description 针对表【relation】的数据库操作Service
* @createDate 2023-10-31 19:51:25
*/
public interface RelationService extends IService<Relation> {
    void link(RelationForm form);

    IPage<UserVo> getGoalPage(Long followersId, PageParams params) ;
}

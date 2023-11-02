package com.gong.weblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.weblog.dto.PageParams;
import com.gong.weblog.entity.Tag;
import com.gong.weblog.vo.TagVo;

/**
* @author asus
* @description 针对表【tag】的数据库操作Service
* @createDate 2023-10-26 14:34:10
*/
public interface TagService extends IService<Tag> {
    IPage<Tag> getTagPage(PageParams params, boolean visible);

    IPage<TagVo> getCategory(PageParams params);

    IPage<TagVo> getPage(PageParams params);
}

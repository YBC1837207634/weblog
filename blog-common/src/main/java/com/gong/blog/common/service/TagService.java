package com.gong.blog.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.entity.Tag;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.vo.TagVo;

import java.util.List;

/**
* @author asus
* @description 针对表【tag】的数据库操作Service
* @createDate 2023-10-26 14:34:10
*/
public interface TagService extends IService<Tag> {
    IPage<Tag> getTagPage(PageParams params, boolean visible, Tag tag);

    IPage<TagVo> getCategory(PageParams params);

    List<TagVo> getTagList();

    IPage<TagVo> getPage(PageParams params);

    IPage<TagVo> getPage(PageParams params, Tag tag);

    List<TagVo> getHotTags();
}

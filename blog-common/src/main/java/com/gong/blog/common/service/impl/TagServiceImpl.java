package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.Tag;
import com.gong.blog.common.mapper.TagMapper;
import com.gong.blog.common.params.PageParams;
import com.gong.blog.common.service.TagService;
import com.gong.blog.common.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author asus
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2023-10-26 14:34:10
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService{

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 根据 visible 返回标签，visible 为true 时返回用于主页展示的标签（类别），false 时全部标签
     * @param params
     * @param visible
     * @return
     */
    @Override
    public IPage<Tag> getTagPage(PageParams params, boolean visible) {
        IPage<Tag> page = new Page<>(params.getPageNum(), params.getPageSize());
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(visible,Tag::getVisible, 1);
        if (StringUtils.hasText(params.getSortField()) && params.getSortField().equals("sortNum")) {
            if (params.getSort().equals("desc")) {
                queryWrapper.orderByDesc(Tag::getSortNum);
            } else {
                queryWrapper.orderByAsc(Tag::getSortNum);
            }
        }
        return tagMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Cacheable(value = "category", key = "#params.toString()")
    public IPage<TagVo> getCategory(PageParams params) {
        IPage<Tag> tagPage = getTagPage(params, true);
        return getVo(tagPage);
    }

    @Cacheable(value = "Taglist", key = "#params.toString()")
    public IPage<TagVo> getPage(PageParams params) {
        IPage<Tag> tagPage = getTagPage(params, false);
        return getVo(tagPage);
    }

    @Cacheable(value = "Taglist", key = "'list'")
    public List<TagVo> getTagList() {
        List<Tag> tags = tagMapper.selectList(Wrappers
                .<Tag>lambdaQuery()
                .orderByAsc(Tag::getSortNum)
                .orderByDesc(Tag::getCreateTime));
        return getVo(tags);
    }

    /**
     * 返回热门标签
     * @return
     */
    @Override
    @Cacheable(value = "hostTag", key = "'list'", unless = "#result.empty")
    public List<TagVo> getHotTags() {
        List<Tag> tags = tagMapper.selectList(Wrappers.<Tag>lambdaQuery().orderByDesc(Tag::getArticleCount));
        return getVo(tags);
    }

    private IPage<TagVo> getVo(IPage<Tag> tagIPage) {
        List<TagVo> vos = new ArrayList<>();
        IPage<TagVo> page = new Page<>();
        BeanUtils.copyProperties(tagIPage, page, "records");
        for (Tag tag : tagIPage.getRecords()) {
            vos.add(copyVo(tag));
        }
        page.setRecords(vos);
        return page;
    }
    private List<TagVo> getVo(List<Tag> tagList) {
        List<TagVo> vos = new ArrayList<>();
        for (Tag tag : tagList) {
            vos.add(copyVo(tag));
        }
        return vos;
    }

    private TagVo copyVo(Tag tag) {
        TagVo vo = new TagVo();
        vo.setId(tag.getId());
        vo.setTagName(tag.getTagName());
        vo.setDescription(tag.getDescription());
        vo.setVisible(tag.getVisible());
        vo.setArticleCount(tag.getArticleCount());
        return vo;
    }

}





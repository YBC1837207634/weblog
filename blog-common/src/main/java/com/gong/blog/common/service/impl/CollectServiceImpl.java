package com.gong.blog.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.common.ActionType;
import com.gong.blog.common.entity.Article;
import com.gong.blog.common.entity.Collect;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.common.exception.NotHaveDataException;
import com.gong.blog.common.exception.ParamException;
import com.gong.blog.common.form.CollectForm;
import com.gong.blog.common.mapper.CollectMapper;
import com.gong.blog.common.params.CollectParams;
import com.gong.blog.common.service.ArticleService;
import com.gong.blog.common.service.CollectService;
import com.gong.blog.common.service.UserService;
import com.gong.blog.common.utils.UserContextUtils;
import com.gong.blog.common.vo.ArticleVo;
import com.gong.blog.common.vo.Favorites;
import com.gong.blog.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author asus
* @description 针对表【collect】的数据库操作Service实现
* @createDate 2023-11-01 10:32:28
*/
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService{

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    /**
     * 创建收藏夹
     */
    @Override
    public void addFavorites(CollectForm form) {
        if (!StringUtils.hasText(form.getName())) {
            throw new CUDException("名称不可为空");
        }
        Collect collect = new Collect();
        collect.setUserId(UserContextUtils.getId());
        collect.setCollectType("1");
        collect.setCollectName(form.getName());
        collect.setAffiliationId(0L);
        // 是否公开
        if (form.getCommon().equals(1)) {
            collect.setCommon(1);
        } else {
            collect.setCommon(0);
        }
        collectMapper.insert(collect);
    }

    /**
     * 给收藏夹中添加项目
     */
    public void addItem(CollectForm form) {
        if (form.getFavoritesIds() == null || form.getFavoritesIds().isEmpty()) {
            throw new NotHaveDataException("收藏失败！");
        }
        if (form.getItemId() == null) {
            throw new CUDException("id不可为空");
        }
        // 判断是否有该文章
        Article article = articleService.getById(form.getItemId());
        if (article == null) {
            throw new NotHaveDataException("数据异常");
        }
        // 检查收藏的文章是否是自己的文章
        if (article.getAuthorId().equals(UserContextUtils.getId())) {
            throw new CUDException("自身作品不可收藏");
        }
        // 不可收藏未公开的文章
        if (article.getCommon().equals(0)) {
            throw new CUDException("操作有误");
        }
        for (long id : form.getFavoritesIds()) {
            // 检查是否有该收藏夹，并且查看当前收藏夹是否是当前用户的收藏夹
            LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Collect::getId, id);
            queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());
            queryWrapper.eq(Collect::getCollectType, "1");  // 是否是收藏夹
            if (collectMapper.selectOne(queryWrapper) == null) {
                log.error("批量收藏文章时没有查到收藏夹");
                throw new CUDException("不存在的收藏夹");
            }
            // 给收藏夹中添加项目
            Collect collect = new Collect();
            collect.setCollectType("2"); // 收藏项
            collect.setUserId(UserContextUtils.getId());
            collect.setItemId(form.getItemId());
            //todo 收藏项目类型
            collect.setItemType("A");   // 文章类型
            collect.setAffiliationId(id);  // 所属收藏夹id
            collectMapper.insert(collect);
        }

    }

    /**
     * 修改收藏
     */
    @Override
    public void itemModify(CollectForm form) {
        // 增加收藏
        if (form.getAct().equals(ActionType.ADD)) {
            addItem(form);
        } else {
            // 删除收藏
            removeItem(form);
        }
    }

    /**
     * 从收藏夹中移除一个或多个收藏项
     */
    public void removeItem(CollectForm form) {
        // 批量删除多个收藏夹中的收藏项
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        // 删除当前用户的收藏项
        queryWrapper.eq(Collect::getCollectType, "2");   // 非文件夹
        queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());  // 是当前用户
        // 批量删除
        if (form.getFavoritesIds() != null && !form.getFavoritesIds().isEmpty()) {
            queryWrapper.in(Collect::getAffiliationId, form.getFavoritesIds());
            // 删除单个
        } else if (form.getFavoritesId() != null) {
            queryWrapper.eq(Collect::getAffiliationId, form.getFavoritesId());
        } else {
            throw new CUDException("取消收藏失败！");
        }
        if (form.getIds() != null &&  !form.getIds().isEmpty()) {
            queryWrapper.in(Collect::getItemId, form.getIds());
        } else if (form.getItemId() != null) {
            queryWrapper.eq(Collect::getItemId, form.getItemId());
        } else {
            throw new CUDException("取消收藏失败！");
        }
        if (collectMapper.delete(queryWrapper) == 0) {
            throw new CUDException("取消收藏失败！");
        }

    }

    /*
    * 操作收藏夹
    * */
    public void favoritesModify(CollectForm form) {
        if (!StringUtils.hasText(form.getName())) {
            throw new CUDException("名称不可为空");
        }
        // 检查当前收藏夹是否存在、是否是文件夹、是否是当前用户
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getId, form.getFavoritesId());
        queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());
        queryWrapper.eq(Collect::getCollectType, "1");  // 是否是收藏夹
        Collect collect = new Collect();
//            collect.setId(form.getFavoritesId());
        collect.setCollectName(form.getName()); // 修改名称
        // 是否公开
        if (form.getCommon().equals(1)) {
            collect.setCommon(1);
        } else {
            collect.setCommon(0);
        }
        collectMapper.update(collect, queryWrapper);

    }

    @Override
    @Transactional
    public void removeFavorites(Long favoritesId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getId, favoritesId);
        queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());
        queryWrapper.eq(Collect::getCollectType, "1");  // 是否是收藏夹
        if (collectMapper.delete(queryWrapper) == 0)
            throw new CUDException("删除失败！");
        queryWrapper.clear();
        queryWrapper.eq(Collect::getAffiliationId, favoritesId); // 删除收藏夹中的收藏项
        collectMapper.delete(queryWrapper);
    }

    @Override
    public Favorites getFavorites(CollectParams params) {
        // 如果没有提供用户的id 则默认查找当前登录用户的收藏夹
        CollectParams collectParams = CollectParams.correctParams(params);
        if (collectParams.getAffiliationId() == null) throw new ParamException("格式错误");
        if (collectParams.getUserId() == null) {
            collectParams.setUserId(UserContextUtils.getId());
        }
        // 查找收藏夹
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getId, collectParams.getAffiliationId());
        queryWrapper.eq(Collect::getCollectType, "1");  //收藏夹
        Collect collect = collectMapper.selectOne(queryWrapper);
        if (collect == null) {
            throw new NotHaveDataException("收藏夹不存在");
        }
        // 分页
        collectParams.setPageSize(collectParams.getPageSize());
        collectParams.setPageNum((collectParams.getPageNum() - 1) * collectParams.getPageSize());
        // 频接数据
        return assemble(collect, collectParams, true, true);
    }

    /**
     * 获取收藏夹列表，没有指定用户id则获取当前用户的收藏夹
     * @param params
     * @return
     */
    @Override
    public List<Favorites> getFavoritesList(CollectParams params) {
        CollectParams collectParams = CollectParams.correctParams(params);
        // 查找收藏夹
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        if (collectParams.getUserId() == null) {
            collectParams.setUserId(UserContextUtils.getId());
            queryWrapper.eq(Collect::getUserId, collectParams.getUserId());
        } else if (collectParams.getUserId().equals(UserContextUtils.getId())) {
            queryWrapper.eq(Collect::getUserId, collectParams.getUserId());
        } else {
            // 如果查询别人的收藏夹则需要该收藏夹是公开的
            queryWrapper.eq(Collect::getUserId, collectParams.getUserId());
            queryWrapper.eq(Collect::getCommon, 1);
        }
        queryWrapper.eq(Collect::getCollectType, "1");  //收藏夹
        if (collectParams.getSort().equals("desc"))
            queryWrapper.orderByDesc(Collect::getCreateTime);
        else {
            queryWrapper.orderByAsc(Collect::getCollectName);
        }
        List<Collect> collects = collectMapper.selectList(queryWrapper);
        List<Favorites> list = new ArrayList<>();
        for (Collect collect : collects) {
            list.add(assemble(collect, collectParams, true, false));
        }
        return list;
    }

    /**
     * 是否收藏所查询的文章，返回具有收藏文章的收藏夹id
     * @return
     */
    @Override
    public List<String> queryIsCollect(CollectParams params) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        // 从用户的所有文件夹中查询是否收藏该文件
        queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());
        queryWrapper.eq(Collect::getItemId, params.getArticleId());
        List<Collect> collects = collectMapper.selectList(queryWrapper);
        List<String> strings = collects.stream().map(item -> Long.toString(item.getAffiliationId())).toList();
        return strings.stream().distinct().toList();
    }

    /**
     * 拼接数据
     * @param collect
     * @param params
     * @param user
     * @param item
     * @return
     */
    private Favorites assemble(Collect collect, CollectParams params, boolean user, boolean item) {
        Favorites favorites = new Favorites();
        // 基本信息
        favorites.setCommon(collect.getCommon());
        favorites.setTitle(collect.getCollectName());
        favorites.setId(collect.getId());
        favorites.setUserId(collect.getUserId());
        favorites.setCreateTime(collect.getCreateTime());
        // 设置每个收藏夹内收藏项的数量
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getAffiliationId, collect.getId());
        favorites.setTotal(collectMapper.selectCount(queryWrapper));
        if (user) {
            UserVo userVo = userService.assembleUserVo(userService.getById(params.getUserId()));
            favorites.setUser(userVo);
        }
        // 如果查询的不是当前用户的收藏夹，并且该文件夹未公开
        if (item && (collect.getUserId().equals(UserContextUtils.getId()) || collect.getCommon().equals(1))) {
            List<Article> articles = collectMapper.selectArticleByAffiliationId(params);
            List<ArticleVo> articleVos = articleService.extraInfoForEach(articles, true, true);
            favorites.setList(articleVos);
        }
        return favorites;
    }

}





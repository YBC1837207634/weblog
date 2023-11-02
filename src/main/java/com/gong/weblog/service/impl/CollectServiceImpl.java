package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.common.ActionType;
import com.gong.weblog.dto.CollectForm;
import com.gong.weblog.dto.CollectParams;
import com.gong.weblog.entity.Article;
import com.gong.weblog.entity.Collect;
import com.gong.weblog.exception.CUDException;
import com.gong.weblog.exception.NotHaveDataException;
import com.gong.weblog.mapper.CollectMapper;
import com.gong.weblog.service.ArticleService;
import com.gong.weblog.service.CollectService;
import com.gong.weblog.service.UserService;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.ArticleVo;
import com.gong.weblog.vo.Favorites;
import com.gong.weblog.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Long addFavorites(CollectForm form) {
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
        return collect.getId();
    }

    /**
     * 给收藏夹中添加项目
     */
    public void addItem(CollectForm form) {
        if (form.getFavoritesId() == null || form.getItemId() == null) {
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
        // 检查是否有该收藏夹，并且查看当前收藏夹是否是当前用户的收藏夹
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getId, form.getFavoritesId());
        queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());
        queryWrapper.eq(Collect::getCollectType, "1");  // 是否是收藏夹
        if (collectMapper.selectOne(queryWrapper) == null) {
            throw new NotHaveDataException("收藏失败！");
        }
        // 给收藏夹中添加项目
        Collect collect = new Collect();
        collect.setCollectType("2"); // 收藏项
        collect.setItemId(form.getItemId());
        //todo 收藏项目类型
        collect.setItemType("A");   // 文章类型
        collect.setAffiliationId(form.getFavoritesId());  // 所属收藏夹id
        collectMapper.insert(collect);

    }

    /**
     * 修改收藏
     */
    @Override
    public void itemModify(CollectForm form) {
        if (form.getFavoritesId() == null) {
            throw new CUDException("id不可为空");
        }
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
        if (form.getFavoritesId() == null || form.getIds().size() == 0) {
            throw new CUDException("参数有误");
        }
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        // 检查收藏夹是否属于当前用户
        queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());
        queryWrapper.eq(Collect::getId, form.getFavoritesId());
        if (collectMapper.selectOne(queryWrapper) == null) {
            throw new CUDException("参数错误");
        }
        queryWrapper.clear();
        // 删除当前用户的收藏项
        queryWrapper.eq(Collect::getCollectType, "2");   // 非文件夹
        queryWrapper.eq(Collect::getAffiliationId, form.getFavoritesId());
        queryWrapper.in(Collect::getItemId, form.getIds());
        if (collectMapper.delete(queryWrapper) == 0)
            throw new CUDException("删除失败！");
    }

    /*
    * 操作收藏夹
    * */
    public void FavoritesModify(CollectForm form) {
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
    public void removeFavorites(Long favoritesId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getId, favoritesId);
        queryWrapper.eq(Collect::getUserId, UserContextUtils.getId());
        queryWrapper.eq(Collect::getCollectType, "1");  // 是否是收藏夹
        if (collectMapper.delete(queryWrapper) == 0)
            throw new CUDException("删除失败！");
    }

    @Override
    public Favorites getFavorites(CollectParams params) {
        // 如果没有提供用户的id 则默认查找当前登录用户的收藏夹
        CollectParams collectParams = CollectParams.correctParams(params);
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





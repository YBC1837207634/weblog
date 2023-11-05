package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.common.ActionType;
import com.gong.weblog.dto.RelationForm;
import com.gong.weblog.dto.RelationParams;
import com.gong.weblog.entity.Relation;
import com.gong.weblog.entity.User;
import com.gong.weblog.exception.CUDException;
import com.gong.weblog.mapper.RelationMapper;
import com.gong.weblog.service.RelationService;
import com.gong.weblog.service.UserService;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author asus
* @description 针对表【relation】的数据库操作Service实现
* @createDate 2023-10-31 19:51:25
*/
@Service
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements RelationService{

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private UserService userService;

    @Override
    public void link(RelationForm form) {
        // 关注
        if (form.getAct().equals(ActionType.ADD)) {
            if (form.getGoalId().equals(UserContextUtils.getId()) || userService.getById(form.getGoalId()) == null)
                throw new CUDException("操作失败！");
            // 查询是否已经关注过
            LambdaQueryWrapper<Relation>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Relation::getFollowersId, UserContextUtils.getId());
            queryWrapper.eq(Relation::getGoalId, form.getGoalId());
            if (relationMapper.selectOne(queryWrapper) == null) {
                Relation relation = new Relation();
                relation.setFollowersId(UserContextUtils.getId());
                relation.setGoalId(form.getGoalId());
                relationMapper.insert(relation);
            }
        } else {
            // 取消关注
            LambdaQueryWrapper<Relation>  queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Relation::getFollowersId, UserContextUtils.getId());
            queryWrapper.eq(Relation::getGoalId, form.getGoalId());
            relationMapper.delete(queryWrapper);
        }
    }

    /*
    * 获取被关注者列表
    * */
    @Override
    public IPage<UserVo> getGoalPage(RelationParams params) {
        if (params.getFollowersId() == null) {
            params.setFollowersId(UserContextUtils.getId());
        }
        IPage<UserVo> userVoIPage = new Page<>();
        List<UserVo> userVos = new ArrayList<>();
        params.setPageNum((params.getPageNum() - 1) * params.getPageSize());
        params.setPageSize(params.getPageSize());
        List<User> goalList = relationMapper.getGoalList(params);
        LambdaQueryWrapper<Relation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Relation::getFollowersId, params.getFollowersId());
        Long count = relationMapper.selectCount(queryWrapper);
        for (User u : goalList) {
            userVos.add(userService.assembleUserVo(u));
        }
        userVoIPage.setRecords(userVos);
        userVoIPage.setSize(userVos.size());
        userVoIPage.setTotal(count);
        return userVoIPage;
    }

    /**
     * 查询用户是否关注某个用户
     * @param goalId
     * @return
     */
    @Override
    public boolean queryRelation(Long goalId) {
        LambdaQueryWrapper<Relation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Relation::getFollowersId, UserContextUtils.getId());
        queryWrapper.eq(Relation::getGoalId, goalId);
        return relationMapper.selectCount(queryWrapper) != 0;
    }


}





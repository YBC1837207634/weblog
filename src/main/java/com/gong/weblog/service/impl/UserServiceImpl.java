package com.gong.weblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.weblog.dto.LoginForm;
import com.gong.weblog.entity.Article;
import com.gong.weblog.entity.Role;
import com.gong.weblog.entity.User;
import com.gong.weblog.exception.ExistException;
import com.gong.weblog.exception.UserException;
import com.gong.weblog.mapper.ArticleMapper;
import com.gong.weblog.mapper.RoleMapper;
import com.gong.weblog.mapper.UserMapper;
import com.gong.weblog.service.UserService;
import com.gong.weblog.utils.JWTUtils;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
* @author asus
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-10-20 10:03:17
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ArticleMapper articleMapper;
    /**
     * 密码校验
     */
    @Override
    public String login(LoginForm loginForm) {
        // 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginForm.getUsername());
        User res = userMapper.selectOne(queryWrapper);
        if (res == null) {
            throw new UserException("账号不存在！");
        }
        if (!res.getStatus().equals("1")) {
            throw new UserException("用户已停用");
        }
        if (passwordEncoder.matches(loginForm.getPassword(), res.getPassword())) {
            return JWTUtils.createToken(res.getId().toString());
        };
        throw new UserException("密码错误");
    }

    /**
     * 注册账号
     * @param form
     * @return
     */
    @Override
    public boolean register(LoginForm form) {
        if (StringUtils.hasText(form.getUsername())
                && StringUtils.hasText(form.getPassword())
                && form.getUsername().length() <= 30
                && form.getPassword().length() <= 30
                && form.getCode() != null && form.getCode().equals("439612")) {
            // 根据用户名查询用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, form.getUsername());
            User res = userMapper.selectOne(queryWrapper);
            if (!Objects.isNull(res)) throw new ExistException("用户已存在");
                // 不存在就注册
                User user = new User();
                user.setUsername(form.getUsername());
                user.setPassword(passwordEncoder.encode(form.getPassword()));
                // 默认的昵称就是用户名
                user.setNickname(form.getUsername());
                userMapper.insert(user);
                return true;
            }
        return false;
    }

    @Override
    public boolean updatePwd(LoginForm form) {
        form.setUsername(null);
        User user = new User();
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setId(UserContextUtils.getId());
        return userMapper.updateById(user) == 0;
    }

    @Override
    public UserVo getUserInfo(Long id) {
        return getUserVo(id);
    }

    /**
     * 返回一个更多信息的user
     * @param id
     * @return
     */
    @Override
    public UserVo getUserVo(Long id) {
        User user = userMapper.selectById(id);
        return assembleUserVo(user);
    }

    /*
    * 将提供的user 转为 vo
    * */
    public UserVo assembleUserVo(User user) {
        if (Objects.nonNull(user)) {
            // 创建作者对象
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            Role role = roleMapper.selectById(user.getRoleId());
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getAuthorId, user.getId());
            userVo.setArticleCount(articleMapper.selectCount(queryWrapper));
            userVo.setRole(role);
            return userVo;
        }
        return new UserVo();
    }

}






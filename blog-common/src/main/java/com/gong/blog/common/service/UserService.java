package com.gong.blog.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.entity.User;
import com.gong.blog.common.form.LoginForm;
import com.gong.blog.common.vo.UserVo;

/**
* @author asus
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-20 10:03:17
*/
public interface UserService extends IService<User> {

    public String login(LoginForm loginForm);

    public boolean register(LoginForm form);

    public boolean updatePwd(LoginForm form);

    UserVo getUserInfo(Long id);

    UserVo getUserVo(Long id);

    UserVo assembleUserVo(User user);

    void logout();
}

package com.gong.blog.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.entity.User;
import com.gong.blog.common.form.LoginForm;
import com.gong.blog.common.form.UserForm;
import com.gong.blog.common.vo.UserVo;
import org.springframework.data.redis.core.HashOperations;

import java.util.List;

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

    List<UserVo> getUserByIds(List<Long> uids);

    UserVo getUserVo(Long id);

    UserVo assembleUserVo(User user);

    UserVo getUserVoByCache(Long userId, HashOperations<String, String, String> hashOperations);

    UserVo getUserVoByCache(Long userId);

    void logout();

    void updateUserInfo(UserForm form);

    List<User> getUserRank(int num);
}

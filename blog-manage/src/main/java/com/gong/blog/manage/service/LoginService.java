package com.gong.blog.manage.service;

import com.gong.blog.manage.dto.LoginForm;

public interface LoginService {

    String login(String username, String password);

    boolean register(LoginForm form);

}

package com.gong.weblog.vo;

import com.gong.weblog.entity.Role;
import com.gong.weblog.entity.User;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserVo extends User {

    private static final long serialVersionUID = 1L;

    private Role role;

    private Long followCount;

    private Long fansCount;

    private Long articleCount;
}

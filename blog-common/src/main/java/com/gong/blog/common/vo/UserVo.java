package com.gong.blog.common.vo;

import com.gong.blog.common.entity.Role;
import com.gong.blog.common.entity.User;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserVo extends User {

    private static final long serialVersionUID = 2L;

    private Role role;

    private Long followCount;

    private Long fansCount;

    private Long articleCount;
}

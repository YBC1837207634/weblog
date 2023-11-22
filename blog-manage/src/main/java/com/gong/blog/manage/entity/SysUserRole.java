package com.gong.blog.manage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /* id */
    private Long id;
    /* 用户id */
    private Long userId;
    /* 角色id */
    private Long roleId;
}

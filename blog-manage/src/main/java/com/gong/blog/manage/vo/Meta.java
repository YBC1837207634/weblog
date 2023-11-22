package com.gong.blog.manage.vo;

import lombok.Data;

/**
 * 路由元信息
 */
@Data
public class Meta {
    // 是否缓存
    private Integer cache;
    // 图标
    private String icon;
    // 路由名称
    private String title;
    // 是否是侧边导航
    private boolean aside = false;
}

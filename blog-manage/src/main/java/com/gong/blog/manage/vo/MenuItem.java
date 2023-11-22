package com.gong.blog.manage.vo;

import lombok.Data;

import java.util.List;

/**
 * 菜单树
 */
@Data
public class MenuItem {
    private long id;
    private String label;
    private List<MenuItem> children;
}

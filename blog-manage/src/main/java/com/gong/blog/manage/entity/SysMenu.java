package com.gong.blog.manage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class SysMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /* id */
    private Long id;

    /* 父菜单id */
    @NotNull(message = "父id不可为空")
    private Long parentId;

    /* 组件名称 */
    private String name;

    /* 菜单名称 */
    @NotBlank(message = "菜单名不可为空")
    private String menuName;

    /* 菜单图标 */
    private String icon;

    /* 路由 */
    private String path;

    /* 组件位置 */
    private String component;

    /* 是否为侧边菜单 */
    private Integer aside;

    /* M目录，C菜单，B按钮 */
    @NotNull(message = "菜单类型不可为空")
    @Pattern(regexp = "[MCB]", message = "只可以提供M/C/B")
    private String menuType;

    /* 0 不缓存组件 1 缓存 */
    private Integer cache;

    /* 状态 */
    private Integer status;

    /* 权限标识 */
    private String purview;

    /* 注释 */
    private String remark;

    /* 排序字段 */
    private Integer orderMenu;

    /* 添加人 */
    private Long createBy;

    /* 修改人 */
    private Long updateBy;

    /* 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /* 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Objects.nonNull(name)) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        if (Objects.nonNull(icon)) {
            this.icon = icon.trim();
        } else {
            this.icon = null;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (Objects.nonNull(path)) {
            this.path = path.trim();
        } else {
            this.path = null;
        }
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        if (Objects.nonNull(component)) {
            this.component = component.trim();
        } else {
            this.component = null;
        }
    }

    public Integer getAside() {
        return aside;
    }

    public void setAside(Integer aside) {
        this.aside = aside;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType.trim();
    }

    public Integer getCache() {
        return cache;
    }

    public void setCache(Integer cache) {
        this.cache = cache;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPurview() {
        return purview;
    }

    public void setPurview(String purview) {
        if (Objects.nonNull(purview)) {
            this.purview = purview.trim();
        } else {
            this.purview = null;
        }
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (Objects.nonNull(remark)) {
            this.remark = remark.trim();
        } else {
            this.remark = null;
        }
    }

    public Integer getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(Integer orderMenu) {
        this.orderMenu = orderMenu;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", menuName='" + menuName + '\'' +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", aside=" + aside +
                ", menuType='" + menuType + '\'' +
                ", cache=" + cache +
                ", status=" + status +
                ", purview='" + purview + '\'' +
                ", remark='" + remark + '\'' +
                ", orderMenu=" + orderMenu +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

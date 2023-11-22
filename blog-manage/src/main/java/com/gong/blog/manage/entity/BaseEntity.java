package com.gong.blog.manage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer page = 1;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer pageSize = 10;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String beginTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String endTime;

    // 需要进行排序的字段
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String orderByColumn;

    // 排序顺序
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String isAsc;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getParams() {
        if (Objects.isNull(this.params)) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "params=" + params +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }
}

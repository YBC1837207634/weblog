package com.gong.blog.common.params;

import lombok.Data;

@Data
public class RelationParams {
    private Integer pageNum = 1;

    private Integer pageSize = 20;

    // 排序字段
    private String sortField = "create_time";

    private String sort = "desc";
    private Long followersId;
}

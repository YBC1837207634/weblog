package com.gong.weblog.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PageParams {

    private Integer pageNum = 1;

    private Integer pageSize = 20;

    // 排序字段
    private String sortField = "create_time";

    private String sort = "desc";

    public static PageParams correctParams(PageParams params) {
        // 查询条件
        ArticleParams p = new ArticleParams();
        if (params.getSort().equals("desc")) {
            p.setSort("desc");
        } else {
            p.setSort("asc");
        }
        String field = params.getSortField();
        if (StringUtils.hasText(field)) {
                if (field.equals("createTime") || field.equals("create_time")) {
                    p.setSortField("create_time");
                } else if (field.equals("update_time") || field.equals("updateTime")) {
                    p.setSortField("update_time");
                }
        }
        return p;
    };

}

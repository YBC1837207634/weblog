package com.gong.blog.common.params;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CollectParams {

    // 收藏夹id
    private Long affiliationId;

    private Long userId;

    private Long articleId;

    private Integer pageNum = 1;

    private Integer pageSize = 20;

    // 排序字段
    private String sortField = "create_time";

    private String sort = "desc";

    public static CollectParams correctParams(CollectParams params) {
        CollectParams p = new CollectParams();
        PageParams p2 = new PageParams();
        PageParams pageParams = PageParams.correctParams(p2);
        BeanUtils.copyProperties(pageParams, p);
        if (p.getSortField().equals("view_count") || p.getSortField().equals("viewCount")) {
            p.setSortField("view_count");
        }
//        if (params.getAffiliationId() == null)
//            throw new ParamException("格式错误");
        p.setAffiliationId(params.getAffiliationId());
        p.setUserId(params.userId);
        return p;
    }

}

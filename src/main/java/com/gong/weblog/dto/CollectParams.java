package com.gong.weblog.dto;

import com.gong.weblog.exception.ParamException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class CollectParams extends PageParams {

    private Long affiliationId;

    private Long userId;

    public static CollectParams correctParams(CollectParams params) {
        CollectParams p = new CollectParams();
        PageParams pageParams = PageParams.correctParams(params);
        BeanUtils.copyProperties(pageParams, p);
        if (params.getSortField().equals("view_count") || pageParams.equals("viewCount")) {
            p.setSortField("view_count");
        }
        if (params.getAffiliationId() == null)
            throw new ParamException("格式错误");
        p.setAffiliationId(params.getAffiliationId());
        p.setUserId(params.userId);
        return p;
    }

}

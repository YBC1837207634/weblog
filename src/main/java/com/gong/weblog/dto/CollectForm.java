package com.gong.weblog.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class CollectForm {

    // 操作类型
    @Pattern(regexp = "[12]", message = "params error")
    private String act;

    // 收藏夹名称
    @Size(max = 80, message = "收藏夹名称不可超过80个字符")
    private String name;

    // 项目id
    @Size(max = 100, message = "单次批量删除不超过100个")
    private Set<Long> ids;

    @Range(min = 1000000000000000000L, message = "id格式有误")
    private Long itemId;

    @Range(min = 1000000000000000000L, message = "id格式有误")
    private Long favoritesId;

    @Size(max = 20, message = "单次批量删除不超过20个")
    private List<Long> favoritesIds;

    @Range(min = 0, max = 1, message = "格式不正确")
    private Integer common = 1;

}

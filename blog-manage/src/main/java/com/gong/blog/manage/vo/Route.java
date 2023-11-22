package com.gong.blog.manage.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class Route {
    private String path;
    private String name;
    private String Component;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String redirect;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Route> children;
    private Meta meta;
}

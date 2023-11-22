package com.gong.blog.common.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RelationForm {

    @Range(min = 1000000000000000000L, message = "id格式有误")
    private Long goalId;

    @Pattern(regexp = "[01]", message="格式错误")
    @NotBlank(message = "不可为空")
    private String act;
}

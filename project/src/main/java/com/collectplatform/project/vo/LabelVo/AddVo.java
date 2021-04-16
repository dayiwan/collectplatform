package com.collectplatform.project.vo.LabelVo;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class AddVo {
    @NotBlank
    private String parentId;
    @NotBlank
    private String name;

}

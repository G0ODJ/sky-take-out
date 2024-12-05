package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("分类管理传递的数据")
public class CategoryDTO implements Serializable {

    //主键
    private Long id;

    //类型 1 菜品分类 2 套餐分类
    @ApiModelProperty(value = "类型（1 菜品分类 2 套餐分类）")
    private Integer type;

    //分类名称
    @ApiModelProperty(value = "分类名称")
    private String name;

    //排序
    @ApiModelProperty(value = "排序")
    private Integer sort;

}

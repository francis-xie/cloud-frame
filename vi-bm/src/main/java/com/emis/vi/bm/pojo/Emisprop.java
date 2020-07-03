package com.emis.vi.bm.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统参数
 */
@Getter
@Setter
public class Emisprop {
    @ApiModelProperty(value = "变数名称")
    private String name;

    @ApiModelProperty(value = "变数值")
    private String value;

    @ApiModelProperty(value = "型态")
    private String kind;

    @ApiModelProperty(value = "变数含义")
    private String remark;

    @ApiModelProperty(value = "变数说明")
    private String remark2;

    @ApiModelProperty(value = "变数分类")
    private String menu;

    @ApiModelProperty(value = "修改人")
    private String updUser;

    @ApiModelProperty(value = "修改日期")
    private String updDate;

    @ApiModelProperty(value = "变数权限")
    private String isroot;
}

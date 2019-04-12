package com.zsyc.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "商品列表查询")
public class GoodsInfoListSearchVO extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称",required = false)
    private String goodsName;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id",required = false)
    private Long categoryId;

    /**
     * 商品sku码 唯一商品
     */
    @ApiModelProperty(value = "商品sku码",required = false)
    private String sku;

    /**
     * 商品spu码 同款商品
     */
    @ApiModelProperty(value = "商品spu码",required = false)
    private String spu;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态",required = false)
    private String status;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型",required = false)
    private Integer goodsStyle;

    /**
     * 商品sku码 唯一商品
     */
    @ApiModelProperty(value = "商品sku码",hidden = true)
    private List<String> skus;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间",required = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间",required = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterTime;


}

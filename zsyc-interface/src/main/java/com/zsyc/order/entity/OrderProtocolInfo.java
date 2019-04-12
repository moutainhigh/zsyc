package com.zsyc.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 协议总表
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(description = "协议信息")
@TableName(value = "order_protocol_info")
public class OrderProtocolInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 协议Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "协议Id")
    private Long id;

    /**
     * 协议名称
     */
    @ApiModelProperty(value = "协议名称")
    private String protocolName;

    /**
     * 用户地址id
     */
    @ApiModelProperty(value = "用户地址id")
    private Long memberAddressId;

    /**
     * 门店Id
     */
    @ApiModelProperty(value = "门店Id")
    private Long storeId;

    /**
     * 协议号
     */
    @ApiModelProperty(value = "协议号")
    private String protocolNo;

    /**
     * 总押金
     */
    @ApiModelProperty(value = "总押金")
    private Integer depositAmount;

    /**
     * 当前押金
     */
    @ApiModelProperty(value = "当前押金")
    private Integer depositCurrent;

    /**
     * 押的sku总数量
     */
    @ApiModelProperty(value = "押的sku总数量")
    private Integer num;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;


    /**
     * 最后一次缴纳租金时间
     */
    @ApiModelProperty(value = "最后一次缴纳租金时间")
    private LocalDateTime lastRentPaymentTime;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    @ApiModelProperty(value = "更新人ID")
    private Long updateUserId;


    public enum ProtocolType implements IEnum {
        RENT("租"), BORROW("借");
        private String desc;
        private ProtocolType(String desc){
            this.desc = desc;
        }

        @Override
        public String desc() {
            return this.desc;
        }

        @Override
        public String val() {
            return this.name();
        }
    }


}

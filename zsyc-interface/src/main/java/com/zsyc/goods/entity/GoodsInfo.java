package com.zsyc.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品总表
 * </p>
 *
 * @author MP
 * @since 2019-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品sku码 唯一商品
     */
    private String sku;

    /**
     * 商品spu码 同款商品
     */
    private String spu;

    /**
     * 1.普通商品，2.组合商品
     */
    private Integer goodsType;

    /**
     * 多张图片逗号分割
     */
    private String picture;

    /**
     * 状态
     */
    private String status;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品类型
     */
    private Integer goodsStyle;

    public enum GoodsInfoEnum implements IEnum{

        ON_SALE("上架"),
        OFF_SALE("下架");
        private String desc;

        GoodsInfoEnum(String desc) {
            this.desc = desc;
        }

        @Override
        public String desc() {
            return this.desc;
        }

        @Override
        public String val() {
            return null;
        }
    }

    public enum GoodsInfoTypeEnum implements IEnum{

        GENERAL_GOODS("普通商品",1),
        GOODS_OF_JOINT("组合商品",2);

        private String desc;

        private Integer type;

        GoodsInfoTypeEnum(String desc, Integer type) {
            this.desc = desc;
            this.type = type;
        }

        public Integer type(){
            return this.type;
        }

        @Override
        public String desc() {
            return null;
        }

        @Override
        public String val() {
            return null;
        }
    }
}

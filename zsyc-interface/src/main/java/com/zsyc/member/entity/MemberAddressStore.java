package com.zsyc.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 地址类型门店关联表
 * </p>
 *
 * @author MP
 * @since 2019-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberAddressStore extends BaseBean {

    private static final long serialVersionUID = 1L;

    @Override
    public Long getId() {
        return id;
    }


    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }



    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 地址ID
     */
    private Long addressId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 地址类型
     */
    private String addressType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 是否为自定义
     */
    private Integer isCustom;




    public enum AddressType implements IEnum {

        NORMAL("普通地址"), VIP("vip地址"), ;
        private String desc;

        private AddressType(String desc){
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

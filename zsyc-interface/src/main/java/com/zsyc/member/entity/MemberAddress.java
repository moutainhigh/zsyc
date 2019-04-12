package com.zsyc.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员地址
 * </p>
 *
 * @author MP
 * @since 2019-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberAddress extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
//    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 手机
     */
    private String telephone;

    /**
     * area_id
     */
    private Long areaId;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 楼层(负数表示电梯户)
     */
    private Integer storey;

    /**
     * 经度
     */
    private Double lat;

    /**
     * 纬度
     */
    private Double lng;

    /**
     * 1:先生，2:女士
     */
    private Integer sex;

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
     * 是否默认,1:默认,2:非默认
     */
    private Integer isDefault;

    /**
     * 燃气卡图片
     */
    private String cardImg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 地址标签，公司，家，学校，单位
     */
    private String tag;

    private String locationAddress;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStorey() {
        return storey;
    }

    public void setStorey(Integer storey) {
        this.storey = storey;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Long getAdcode() {
        return adcode;
    }

    public void setAdcode(Long adcode) {
        this.adcode = adcode;
    }

    /**
     * 冗余area_id，行政区域代码
     */
    private Long adcode;


}

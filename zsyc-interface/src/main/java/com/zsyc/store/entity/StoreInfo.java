package com.zsyc.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.zsyc.IEnum;
import com.zsyc.framework.base.BaseBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺详情表
 * </p>
 *
 * @author MP
 * @since 2019-02-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StoreInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private Integer adcode;

    /**
     * 门店名，不可重复
     */
    private String storeName;

    /**
     * 门店联系电话
     */
    private String phone;

    /**
     * 地区ID，外键
     */
    private Integer areaId;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 状态 NORMAL-正常，SCRAP-废弃
     */
    private String status;

    /**
     * 打印机名字
     */
    private String printer;

    /**
     * 是否可定价 CAN-可以，NOT-禁止
     */
    private String pricing;

    /**
     * 门店类型id
     */
    private Long storeTypeId;

    /**
     * 门店照片
     */
    private String picture;

    /**
     * 门店楼层运费
     */
    private Integer carriage;

    /**
     * 上门收瓶服务费
     */
    private Integer serviceCharge;

    /**
     * 门店自设租金
     */
    private Integer rent;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    @TableField("Latitude")
    private Double Latitude;

    /**
     * 覆盖半经
     */
    private Integer radius;

    /**
     * 店铺编号
     */
    private String storeNo;

    /**
     * 营业执照及证件
     */
    private String license;

    /**
     * 经营范围
     */
    private String scopeBusiness;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建用户id
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新操作的用户id
     */
    private Long updateUserId;

    /**
     * 是否删除
     */
    private Integer isDel;



    public enum Pricing implements IEnum {

        CAN("可以定价"), NOT("不可定价"), ;
        private String desc;

        private Pricing(String desc){
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


    //刷新备货时间间隔（分钟）
    private Integer intervalTime;

    //提前的时间，（分钟）
    private Integer presetTime;

    //定时任务状态 1：启动 0：关闭
    private Integer intervalState;






    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public Long getStoreTypeId() {
        return storeTypeId;
    }

    public void setStoreTypeId(Long storeTypeId) {
        this.storeTypeId = storeTypeId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getCarriage() {
        return carriage;
    }

    public void setCarriage(Integer carriage) {
        this.carriage = carriage;
    }

    public Integer getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Integer serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getRent() {
        return rent;
    }

    public void setRent(Integer rent) {
        this.rent = rent;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getScopeBusiness() {
        return scopeBusiness;
    }

    public void setScopeBusiness(String scopeBusiness) {
        this.scopeBusiness = scopeBusiness;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
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


}

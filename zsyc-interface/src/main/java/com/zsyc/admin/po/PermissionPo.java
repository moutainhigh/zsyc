package com.zsyc.admin.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsyc.framework.base.BaseBean;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermissionPo extends BaseBean {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级Id
     */
    private Long parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限值
     */
    private String value;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限类型
     */
    private String type;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * tree path
     */
    private String treePath;

    /**
     * tree level
     */
    private Integer treeLevel;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;
    /**
     * 更新人ID
     */
    private Long updateUserId;

    private Long RoleId;

    public static class MenuType{
        public final static String MENU = "menu";
        public final static String BUTTON = "button";
    }
}

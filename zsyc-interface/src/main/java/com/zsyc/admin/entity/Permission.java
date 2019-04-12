package com.zsyc.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.zsyc.framework.base.BaseBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author MP
 * @since 2019-01-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission extends BaseBean {

    private static final long serialVersionUID = 1L;

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

    /**
     * 权限类型
     */
    public static class PermissionType {
        public final static String MENU = "menu";
        public final static String BUTTON = "button";
        public final static String API = "api";
    }

}

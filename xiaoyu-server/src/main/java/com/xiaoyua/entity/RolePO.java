package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 角色字典表
 */
@Data
@TableName("roles")
public class RolePO {

    /** 角色 ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 角色名称：USER、ADMIN 等 */
    private String name;
}
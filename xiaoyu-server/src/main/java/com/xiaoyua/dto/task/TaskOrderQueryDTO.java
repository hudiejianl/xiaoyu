package com.xiaoyua.dto.task;

import com.xiaoyua.dto.common.PageDTO;

/**
 * 任务订单查询 DTO
 * 用于处理任务订单查询请求
 * 
 * @author xiaoyu
 */
public class TaskOrderQueryDTO extends PageDTO {
    
    /**
     * 角色类型：PUBLISHER-发布者，RECEIVER-接单者
     * 用于区分查询发布的任务订单还是接取的任务订单
     */
    private String roleType;
    
    /**
     * 订单状态：WAIT_ACCEPT-等待接受，ACCEPTED-已接受，REFUSED-已拒绝，CANCELLED-已取消，FINISH-已完成
     * 为空时查询所有状态的订单
     */
    private String status;
    
    public TaskOrderQueryDTO() {}
    
    public TaskOrderQueryDTO(String roleType, String status) {
        this.roleType = roleType;
        this.status = status;
    }
    
    public String getRoleType() {
        return roleType;
    }
    
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "TaskOrderQueryDTO{" +
                "roleType='" + roleType + '\'' +
                ", status='" + status + '\'' +
                ", page=" + getPage() +
                ", size=" + getSize() +
                '}';
    }
}
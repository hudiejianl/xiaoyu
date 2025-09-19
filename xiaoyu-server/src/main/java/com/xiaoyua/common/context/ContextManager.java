package com.xiaoyua.common.context;

import com.xiaoyua.context.BaseContext;

/**
 * 上下文管理器 - 栈式自动清理
 * 
 * @author xiaoyu
 */
public class ContextManager implements AutoCloseable {
    
    private final Long previousUserId;
    
    private ContextManager(Long userId) {
        this.previousUserId = BaseContext.getCurrentId();
        BaseContext.setCurrentId(userId);
    }
    
    /**
     * 设置用户上下文（自动清理）
     */
    public static ContextManager withUser(Long userId) {
        return new ContextManager(userId);
    }
    
    @Override
    public void close() {
        BaseContext.setCurrentId(previousUserId);
    }
}
